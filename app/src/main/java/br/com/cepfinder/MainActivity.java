package br.com.cepfinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import br.com.cepfinder.Model.CEP;
import br.com.cepfinder.Retrofit.RetrofitInitialization;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static org.ksoap2.serialization.MarshalHashtable.NAMESPACE;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private TextView textView;

    private EditText editSoap;
    private Button buttonSoap;
    private TextView resultadoSoap;

    String URL = "http://www.dataaccess.com/webservicesserver/numberconversion.wso?WSDL";
    String NAMESPACE = "http://www.dataaccess.com/webservicesserver/";
    String SOAP_ACTION = "http://com/square";
    String METHOD_NAME = "NumberToWords";
    String PARAMETER_NAME = "ubiNum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.buttonSoap);
        textView = findViewById(R.id.resultSoap);

        buttonSoap = findViewById(R.id.buttonSoap);
        resultadoSoap = findViewById(R.id.resultSoap);
        editSoap = findViewById(R.id.editSoap);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCep(editText.getText().toString());

            }
        });

        buttonSoap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CallWebService().execute(editSoap.getText().toString());
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

    class CallWebService extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            resultadoSoap.setText("Square = " + s);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(PARAMETER_NAME);
            propertyInfo.setValue(params[0]);
            propertyInfo.setType(String.class);

            soapObject.addProperty(propertyInfo);

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(soapObject);

            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

            try {
                httpTransportSE.call(SOAP_ACTION, envelope);
                SoapPrimitive soapPrimitive = (SoapPrimitive)envelope.getResponse();
                result = soapPrimitive.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
