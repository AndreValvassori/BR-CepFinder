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


public class MainActivity extends AppCompatActivity {

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


        buttonSoap = findViewById(R.id.buttonSoap);
        resultadoSoap = findViewById(R.id.resultSoap);
        editSoap = findViewById(R.id.editSoap);


        buttonSoap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CallWebService().execute(editSoap.getText().toString());
            }
        });
    }

    class CallWebService extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            resultadoSoap.setText("result = " + s);
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
