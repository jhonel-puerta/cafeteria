package com.example.cafeteria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cafeteria.Interface.JsonPlaceHolder;
import com.example.cafeteria.Model.productos;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    //ultimos 3 productos comprados recientemente
    public TextView recent1;
    public TextView recent2;
    public TextView recent3;

    //images
    public ImageView jpc;

    public Button btnRevertir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recent1 = findViewById(R.id.recent1);
        recent2 = findViewById(R.id.recent2);
        recent3 = findViewById(R.id.recent3);

        //imagenes
        //jpc.findViewById(R.id.ij);

        btnRevertir = findViewById(R.id.btnRevertir);

        getProductos();

        btnRevertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_revertir_compra,null);
        alert.setView(view);
        AlertDialog dialog = alert.create();
        dialog.show();

        Button btnPuntos = dialog.findViewById(R.id.btnPuntos);
        Button btnEfectivo = dialog.findViewById(R.id.btnEfectivo);

        btnPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "No tienes Puntos suficientes para canjear este producto.", Toast.LENGTH_SHORT).show();
            }
        });

        btnEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                hacerPedido();
            }
        });

    }

    private void hacerPedido(){
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this,R.style.NewDialog);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.confirmation_alert,null);
        alert.setView(view);
        AlertDialog dialog = alert.create();
        dialog.show();

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Toast.makeText(getApplicationContext(), "segundo Numero: "  + millisUntilFinished / 1000 , Toast.LENGTH_SHORT).show();
            }

            public void onFinish() {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Se agregaron 40 puntos a su cuenta.", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }



    private void getProductos(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.198.194/api_rest_cafeteria/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolder jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);

        Call<List<productos>> call = jsonPlaceHolder.getProducts();
        call.enqueue(new Callback<List<productos>>() {
            @Override
            public void onResponse(Call<List<productos>> call, Response<List<productos>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Error" + response.code(), Toast.LENGTH_SHORT).show();
                }

                List<productos> list = response.body();
                ArrayList<String> nombres = new ArrayList<String>();

                //creamos listas por categorias para nombres, categoria, precio tdo en listas
                for (productos products: list){
                    nombres.add(products.getNombre().toString());
                }

                //getImgaes:
                String UrlImg = "http://192.168.198.194/api_rest_cafeteria/images/queue_vainilla.jpg";
                //Glide.with(getApplicationContext()).load(UrlImg).into(ImgCafe);

                recent1.setText(nombres.get(0).toString());
                recent2.setText(nombres.get(1).toString());
                recent3.setText(nombres.get(2).toString());
            }
            @Override
            public void onFailure(Call<List<productos>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getImages(){

    }

}