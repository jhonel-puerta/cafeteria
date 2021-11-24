package com.example.cafeteria.Interface;

import com.example.cafeteria.Model.productos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolder {

    @GET("registros.php")
    Call<List<productos>> getProducts();

}
