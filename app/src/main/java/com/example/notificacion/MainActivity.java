package com.example.notificacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    EditText txtGrupo;
    Button btnRegistrar;
    Button btnConectarBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGrupo = findViewById(R.id.txtGrupo);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el nombre del grupo ingresado por el usuario desde el TextBox
                String nombreGrupo = txtGrupo.getText().toString().trim();

                // Verificar si se ingresó un nombre de grupo válido
                if (!nombreGrupo.isEmpty()){
                    FirebaseMessaging.getInstance().subscribeToTopic(nombreGrupo)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    String msg;
                                    if (task.isSuccessful()) {
                                        msg = "¡Te has suscrito al grupo " + nombreGrupo + "!";
                                    } else {
                                        msg = "Error al suscribirse al grupo " + nombreGrupo;
                                    }
                                    Log.d(TAG, msg);
                                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(MainActivity.this, "Por favor, ingrese un nombre de grupo válido", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnConectarBD=findViewById(R.id.btnConectarBD);
        btnConectarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conexion();
            }
        });


    }

    public Connection conexion(){
        Connection cnn=null;
        try {
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://DESKTOP-7J8127S:1433;databaseName=CONEXION;user=sa;password=0960184527Alex;");
            if (cnn != null && !cnn.isClosed()) {
                Toast.makeText(getApplicationContext(), "Conexión exitosa", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return cnn;
    }



    public void consulta(){
        try {
            Statement stm = conexion().createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM VISTA");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}