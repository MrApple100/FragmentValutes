package com.example.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class ValuteFragment extends Fragment {

    Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1,container,false);


        ArrayList<Valute> valutes = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.RV);
        AdapterValute adapterValute = new AdapterValute(valutes);
        recyclerView.setAdapter(adapterValute);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                ArrayList<Valute> valutes =(ArrayList<Valute>) msg.obj;
                AdapterValute adapterValute = new AdapterValute(valutes);
                recyclerView.setAdapter(adapterValute);

            }
        };

        DataThread thread = new DataThread();
        thread.start();

        return view;
    }




    class DataThread extends  Thread{

        @Override
        public void run() {
            super.run();


            String infoResult = "";
            String flags="";

            ArrayList<Valute> valutes = new ArrayList<>();

            Bitmap bitmap = null;
            try {
                URL info = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
                URL pictures = new URL("https://gist.github.com/sanchezzzhak/8606e9607396fb5f8216/raw/8a7209a4c1f4728314ef4208abc78be6e9fd5a2f/ISO3166_RU.json");
                Scanner scanner = new Scanner(info.openStream());
                while(scanner.hasNext()){
                    infoResult += scanner.nextLine();
                }
                scanner.close();
                scanner = new Scanner(pictures.openStream());
                while(scanner.hasNext()){
                    flags+=scanner.nextLine();
                }
                scanner.close();

                JSONObject json = new JSONObject(infoResult).getJSONObject("Valute");
                JSONArray jsonflags = new JSONArray(flags);
                for(int i=0;i<json.names().length();i++){
                    JSONObject jsonValute = json.getJSONObject(json.names().getString(i));
                    String charCode = jsonValute.getString("CharCode").substring(0,2);

                    for(int j=0;j<jsonflags.length();j++){
                        String charCodeFlag = jsonflags.getJSONObject(j).getString("iso_code2");
                        if(charCodeFlag.compareTo(charCode)==0){
                            URL urlbit = new URL("https:"+jsonflags.getJSONObject(j).getString("flag_url"));
                            HttpsURLConnection con =(HttpsURLConnection) urlbit.openConnection();

                            // con.setReadTimeout(1500);
                            //con.setConnectTimeout(1500);
                            con.connect();

                            int responseCode = con.getResponseCode();

                            if(responseCode == 200){
                                InputStream inputStream = con.getInputStream();
                                bitmap = BitmapFactory.decodeStream(inputStream);

                            }
                        }
                    }
                    valutes.add(new Valute(jsonValute.getString("Value"),jsonValute.getString("Name"),bitmap));
                }


            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Message msg = new Message();
            msg.obj = valutes;
            handler.sendMessage(msg);
        }
    }
}
