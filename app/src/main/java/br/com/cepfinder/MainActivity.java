package br.com.cepfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.cepfinder.Model.CEP;
import br.com.cepfinder.Retrofit.RetrofitInitialization;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.endereco);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCep(editText.getText().toString());

            }
        });
    }

    private void loadCep(String cep) {
        Call<CEP> call = new RetrofitInitialization().getCepJsonService().getCepJson(cep);
        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                CEP endereco = response.body();
                textView.setText(endereco.getLogradouro() + ", " + endereco.getBairro() + " - " + endereco.getLocalidade() + "/" + endereco.getUf());
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                textView.setText("CEP não encontrado!");
            }
        });
    }
}
