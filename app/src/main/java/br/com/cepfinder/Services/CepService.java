package br.com.cepfinder.Services;

import br.com.cepfinder.Model.CEP;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CepService {
    @GET("{cep}/json")
    Call<CEP> getCepJson(@Path("cep") String cep);
}
