package br.com.cepfinder.Retrofit;

import br.com.cepfinder.Services.CepService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInitialization {
    private final Retrofit retrofit;
    public RetrofitInitialization(){
        String url = "https://viacep.com.br/ws/";
        retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(JacksonConverterFactory.create()).build();
    }
    public CepService getCepJsonService() {
        return retrofit.create(CepService.class);
    }
}