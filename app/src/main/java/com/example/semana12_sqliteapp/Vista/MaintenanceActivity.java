package com.example.semana12_sqliteapp.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.semana12_sqliteapp.Controlador.ConexionHelper;
import com.example.semana12_sqliteapp.Controlador.Utility;
import com.example.semana12_sqliteapp.R;


public class MaintenanceActivity extends AppCompatActivity {

    EditText txtid;
    EditText txtnombre;
    EditText txtcorreo;
    Button btnconsultar, btnupdate, btndelete;

    ConexionHelper conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        conn = new ConexionHelper(getApplicationContext(), "bd_usuarios", null, 1);

        txtid = (EditText) findViewById(R.id.txtId);
        txtnombre = (EditText) findViewById(R.id.txtNombre);
        txtcorreo = (EditText) findViewById(R.id.txtCorreo);

        btnconsultar = (Button) findViewById(R.id.btnSearch);
        btnupdate = (Button) findViewById(R.id.btnUpdate);
        btndelete = (Button) findViewById(R.id.btnDelete);

        btnconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultar();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarUsuario();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarUsuario();
            }
        });
    }

    private void eliminarUsuario() {
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {txtid.getText().toString()};

        db.delete(Utility.TABLA_USUARIO, Utility.CAMPO_ID + "=?", parametros);
        Toast.makeText(getApplicationContext(), "ATENCION, se eliminó el usuario",
                Toast.LENGTH_LONG).show();
        txtid.setText("");
        limpiar();
        db.close();
    }

    private void actualizarUsuario() {
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {txtid.getText().toString()};

        ContentValues values = new ContentValues();
        values.put(Utility.CAMPO_NOMBRE, txtnombre.getText().toString());
        values.put(Utility.CAMPO_CORREO, txtcorreo.getText().toString());

        db.update(Utility.TABLA_USUARIO, values, Utility.CAMPO_ID + "=?", parametros);
        Toast.makeText(getApplicationContext(),"ATENCION, se actualizó el usuario",
                Toast.LENGTH_LONG).show();
        //limpiar();
        txtnombre.setText("");
        txtcorreo.setText("");
        db.close();
    }

    private void consultar() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {txtid.getText().toString()};

        try {
            Cursor cursor = db.rawQuery("SELECT " + Utility.CAMPO_NOMBRE + "," + Utility.CAMPO_CORREO +
                    " FROM " + Utility.TABLA_USUARIO + " WHERE " + Utility.CAMPO_ID + "=? ", parametros);

            cursor.moveToFirst();
            txtnombre.setText(cursor.getString(0));
            txtcorreo.setText(cursor.getString(1));
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "ATENCION, usuario no existe",
                    Toast.LENGTH_LONG).show();
            limpiar();
        }
    }
    private void consultarSql() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={txtid.getText().toString()};

        try {
            //select nombre,telefono from usuario where codigo=?
            Cursor cursor=db.rawQuery("SELECT "+Utility.CAMPO_NOMBRE+","+Utility.CAMPO_CORREO+
                    " FROM "+Utility.TABLA_USUARIO+" WHERE "+Utility.CAMPO_ID+"=? ",parametros);

            cursor.moveToFirst();
            txtnombre.setText(cursor.getString(0));
            txtcorreo.setText(cursor.getString(1));

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El documento no existe",Toast.LENGTH_LONG).show();
            limpiar();
        }
    }

    private void limpiar() {
        txtnombre.setText("");
        txtcorreo.setText("");
    }
}