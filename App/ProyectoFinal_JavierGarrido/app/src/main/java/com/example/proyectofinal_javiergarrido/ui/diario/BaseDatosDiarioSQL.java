package com.example.proyectofinal_javiergarrido.ui.diario;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BaseDatosDiarioSQL extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "diarioDB";
    private static final int VERSION_BD = 1;
    private SQLiteDatabase db;
    private final Context context;

    public BaseDatosDiarioSQL(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_NOTAS = "CREATE TABLE IF NOT EXISTS notas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, " +
                "contenido TEXT, " +
                "fecha TEXT, " +
                "color TEXT);";
        db.execSQL(CREATE_TABLE_NOTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notas");
        onCreate(db);
    }

    public void abrir() throws SQLException {
        db = this.getWritableDatabase();
    }

    public void cerrar() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public long agregarNota(Nota nota) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", nota.getTitulo());
        values.put("contenido", nota.getContenido());
        values.put("fecha", nota.getFecha());
        values.put("color", nota.getColor());

        long id = db.insert("notas", null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<Nota> obtenerNotas() {
        db = this.getReadableDatabase();
        List<Nota> listaNotas = new ArrayList<>();
        Cursor cursor = db.query("notas", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Nota nota = new Nota();
                nota.setId(cursor.getInt(cursor.getColumnIndex("id")));
                nota.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
                nota.setContenido(cursor.getString(cursor.getColumnIndex("contenido")));
                nota.setFecha(cursor.getString(cursor.getColumnIndex("fecha")));
                nota.setColor(cursor.getString(cursor.getColumnIndex("color")));
                listaNotas.add(nota);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return listaNotas;
    }

    public void eliminarNota(Nota nota) {
        db = this.getWritableDatabase();
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(nota.getId())};
        db.delete("notas", whereClause, whereArgs);
        db.close();
    }

    public boolean actualizarNota(Nota nota) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("titulo", nota.getTitulo());
        valores.put("contenido", nota.getContenido());
        valores.put("fecha", nota.getFecha());
        valores.put("color", nota.getColor());

        int filasAfectadas = db.update("notas", valores, "id = ?", new String[]{String.valueOf(nota.getId())});
        db.close();
        return filasAfectadas > 0;
    }
}
