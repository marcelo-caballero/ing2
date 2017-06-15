package com.example.acer.vacuna;

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


import com.example.acer.vacuna.Alarma.AlarmReceiver;
import com.example.acer.vacuna.Alarma.Alarma;
import com.example.acer.vacuna.Alarma.Datos;
import com.example.acer.vacuna.Modelo.Hijo;
import com.example.acer.vacuna.Modelo.Usuario;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    public static final String direccion_ip = "http://192.168.42.55:8080";
    private ListView lv;
    private ArrayList<Hijo> lista_hijos = null;
    ArrayAdapter adaptador;
    private Usuario usuario = null;

    private SignInButton btnSignIn;
    private Button btnSignOut;
    private Button btnRevoke;
    private TextView txtNombre;
    private TextView txtEmail;

    private GoogleApiClient apiClient;
    private static final int RC_SIGN_IN = 1001;

    private ProgressDialog progressDialog;
    private boolean error = false;

    //Alarma
    Datos usdbh = new Datos(this, "DBUsuarios", null, 1);
    Alarma alarma = null;


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
                                usuario = null;
                                error = false;

                                //limpia la lista y se lo notifica al adaptador
                                if(lista_hijos != null){
                                    lista_hijos.clear();
                                    adaptador.notifyDataSetChanged();


                                    lista_hijos = null;
                                }

                                //alarma
                                if(usdbh.getAlarma() == 1){
                                    alarma = new Alarma(getApplicationContext(), AlarmReceiver.class);
                                    alarma.cancel();
                                    usdbh.desprogramarAlarma();
                                }

                                updateUI(false);
                            }
                        });
            }
        });

        btnRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.revokeAccess(apiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                usuario = null;
                                error = false;

                                //limpia la lista y se lo notifica al adaptador
                                if(lista_hijos != null){
                                    lista_hijos.clear();
                                    adaptador.notifyDataSetChanged();


                                    lista_hijos = null;
                                }

                                //alarma
                                if(usdbh.getAlarma() == 1){
                                    alarma = new Alarma(getApplicationContext(), AlarmReceiver.class);
                                    alarma.cancel();
                                    usdbh.desprogramarAlarma();
                                }
                                updateUI(false);
                            }
                        });
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
            //Usuario logueado a la cuenta de gmail
            GoogleSignInAccount acct = result.getSignInAccount();


            //
            DetallesUsuarioRest usu_rest = new DetallesUsuarioRest();

            //congelamos la ejecucion del hilo principal
            //hasta obtener el resultado del servicio rest
            try{
                usu_rest.execute(acct.getEmail()).get(4500,TimeUnit.MILLISECONDS);
            }catch(Exception e){

                error = true;
            }

            if(error) {
                Toast.makeText(MainActivity.this, "Error en la comunicacion", Toast.LENGTH_SHORT).show();

            }else{
                if (usuario != null) {

                    //Alarma
                    usdbh.set_usuario(acct.getEmail());


                    txtNombre.setText(usuario.getNombre());
                    txtEmail.setText(usuario.getCorreo());

                    if (lista_hijos == null) {

                        Toast.makeText(getApplicationContext(), "No tiene hijos", Toast.LENGTH_SHORT).show();
                    } else {
                        //lanzamos la alarma al tener hijos//
                        if (usdbh.getAlarma() == 0) {
                            alarma = new Alarma(getApplicationContext(), AlarmReceiver.class);
                            alarma.start();
                            usdbh.programarAlarma();
                        }


                        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_hijos);
                        lv.setAdapter(adaptador);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), RegistroVacunacion.class);
                                intent.putExtra("ci", lista_hijos.get(position).getCi());
                                intent.putExtra("direccion_ip",direccion_ip);
                                startActivity(intent);
                            }
                        });
                    }

                } else {
                    Toast.makeText(MainActivity.this, "No existe usuario en la BD", Toast.LENGTH_SHORT).show();

                }

            }

            updateUI(true);
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

    /*
    * Servicio rest que valida la existencia del usuario
    * y obtiene la lista de hijos.
    * */
    private class DetallesUsuarioRest extends AsyncTask<String,Void,Void> {
        Usuario usu = null;
        ArrayList<Hijo> list = null;

        protected Void doInBackground(String... params) {


            HttpClient http_usuario = new DefaultHttpClient();


            String correo = params[0];

            //URL del sw para obtener usuario
            HttpGet get_usuario = new HttpGet(direccion_ip + "/rest/webresources/paquete.usuarios/usuario?correo=" + correo);

            get_usuario.setHeader("content-type", "application/json");


            try{
                HttpResponse resp = http_usuario.execute(get_usuario);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                if(respJSON.has("correo")&&respJSON.has("id")&&respJSON.has("nombre")) {
                    usu = new Usuario();

                    usu.setCorreo(respJSON.getString("correo"));
                    usu.setId(respJSON.getInt("id"));
                    usu.setNombre(respJSON.getString("nombre"));
                }

            } catch (Exception ex) {
                error = true;
            }

            //URL para obtener la lista de hijos
            if(usu != null) {
                HttpClient http_hijos = new DefaultHttpClient();
                HttpGet get_hijos =
                        new HttpGet(direccion_ip + "/rest/webresources/paquete.hijos/hijos?correo=" + correo);

                get_hijos.setHeader("content-type", "application/json");
                try
                {
                    HttpResponse resp = http_hijos.execute(get_hijos);
                    String respStr = EntityUtils.toString(resp.getEntity());

                    JSONArray respJSON = new JSONArray(respStr);
                    if(respJSON.length()>0) {
                        list = new ArrayList<Hijo>();
                    }

                    Hijo hijo;
                    for(int i=0; i<respJSON.length();i++){

                        JSONObject obj = respJSON.getJSONObject(i);

                        String apellido = obj.getString("apellido");
                        int ci = obj.getInt("ci");

                        String email = obj.getString("email");
                        String fecha_nac = obj.getString("fechaNac");
                        String nombre = obj.getString("nombre");

                        hijo = new Hijo(ci,nombre,apellido,email,fecha_nac);
                        list.add(hijo);

                    }



                }
                catch(Exception ex)
                {
                    error = true;
                }
            }
            usuario = usu;
            lista_hijos = list;

           return null;

        }


    }


}