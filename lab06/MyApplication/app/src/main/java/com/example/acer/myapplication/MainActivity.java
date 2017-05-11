package com.example.acer.myapplication;



import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private ListView lv;
    //ArrayList<String> lista_vacuna;
    ArrayList<Hijo> lista;
    ArrayAdapter adaptador;
    Alarma alarma = null;
    Usuario usuario = null;

    private SignInButton btnSignIn;
    private Button btnSignOut;
    private Button btnRevoke;
    private TextView txtNombre;
    private TextView txtEmail;

    private GoogleApiClient apiClient;
    private static final int RC_SIGN_IN = 1001;

    private ProgressDialog progressDialog;

    //Base de datos
    BDatos usdbh = new BDatos(this, "DBUsuarios", null, 1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSignIn = (SignInButton)findViewById(R.id.sign_in_button);
        btnSignOut = (Button)findViewById(R.id.sign_out_button);
        btnRevoke = (Button)findViewById(R.id.revoke_button);
        txtNombre = (TextView)findViewById(R.id.txtNombre);
        txtEmail = (TextView)findViewById(R.id.txtEmail);

        lv = (ListView)findViewById(R.id.lista);



        //Google API Client

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Personalización del botón de login

        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setColorScheme(SignInButton.COLOR_LIGHT);
        btnSignIn.setScopes(gso.getScopeArray());

        //Eventos de los botones

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                updateUI(false);

                            }
                        });
                if(usdbh.getAlarma() == 1){
                    alarma = new Alarma(getApplicationContext(), AlarmReceiver.class);
                    alarma.cancel();
                    usdbh.desprogramarAlarma();
                }
            }
        });

        btnRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.revokeAccess(apiClient).setResultCallback(
                        new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {

                                updateUI(false);

                            }
                        });
                if(usdbh.getAlarma() == 1){
                    alarma = new Alarma(getApplicationContext(), AlarmReceiver.class);
                    alarma.cancel();
                    usdbh.desprogramarAlarma();
                }
            }

        });

        updateUI(false);


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!", Toast.LENGTH_SHORT).show();
        Log.e("GoogleSignIn", "OnConnectionFailed: " + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result =
                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            //Usuario logueado --> Mostramos sus datos



            GoogleSignInAccount acct = result.getSignInAccount();
            txtNombre.setText(acct.getDisplayName());
            txtEmail.setText(acct.getEmail());


            //------------------------------------------------------------------
            boolean exc = false;
            boolean existeUsuario = false;
            TareaWSObtener tarea = new TareaWSObtener();
            try {
                tarea.execute(acct.getEmail()).get(3000, TimeUnit.MILLISECONDS);
            }catch(ExecutionException e){
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                exc = true;

            }catch(InterruptedException e){
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                exc = true;
            }
            catch(TimeoutException e){
                exc = true;
                Toast.makeText(MainActivity.this, "no se pudo conectarse con el servidor", Toast.LENGTH_SHORT).show();
            }
            if (usuario != null) {
                existeUsuario = true;
            }
            if(!exc && usuario == null) {
                //no exste correo en la base de datos
                Toast.makeText(MainActivity.this, "no existe correo en la base de datos", Toast.LENGTH_SHORT).show();

            }
            //-------------------------------------------------------------------
            if(existeUsuario) {
                   // guarda el email de usuario en la base de datos//
                usdbh.setUsuarioLogueado(acct.getEmail());

                // Listar hijos
                lista = usdbh.obtener_lista_hijos(acct.getEmail());
                if (lista.isEmpty()) {
                    Toast toast1 =
                               Toast.makeText(getApplicationContext(),
                                       "No tiene hijos", Toast.LENGTH_SHORT);

                       toast1.show();
                } else {
                       //Si tiene hijos, lanzamos la alarma//
                    if (usdbh.getAlarma() == 0) {
                        alarma = new Alarma(getApplicationContext(), AlarmReceiver.class);
                        alarma.start();
                        usdbh.programarAlarma();
                    }

                    adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
                    lv.setAdapter(adaptador);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               Intent intent = new Intent(getApplicationContext(), RegistroVacunacion.class);
                               intent.putExtra("ci", lista.get(position).getCi());
                               startActivity(intent);
                           }
                       });
                }


                updateUI(true);
            }else{
                updateUI(false);
            }
        } else {
            //Usuario no logueado --> Lo mostramos como "Desconectado"
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevoke.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);//
        } else {
            txtNombre.setText("Desconectado");
            txtEmail.setText("Desconectado");

            lv.setVisibility(View.GONE);//

            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevoke.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(apiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Silent SignI-In");
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {

        private int idCli;
        private String nombCli;
        private String correoCli;

        protected void onPreExecute(){
            Toast.makeText(MainActivity.this, "Conectando con el servidor...", Toast.LENGTH_SHORT).show();
        }

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];


            HttpGet del =
                    new HttpGet("http://192.168.42.9:8080/servidorREST/webresources/paquete.usuarios/"+id);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);

                if(respJSON.has("correo") && respJSON.has("id") && respJSON.has("nombre")){
                    correoCli = respJSON.getString("correo");
                    idCli = respJSON.getInt("id");
                    nombCli = respJSON.getString("nombre");
                    usuario = new Usuario(idCli,correoCli,nombCli);

                }


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                Toast.makeText(MainActivity.this, "Error de conexion...", Toast.LENGTH_SHORT).show();
                resul = false;
            }

            return resul;
        }
    }
}