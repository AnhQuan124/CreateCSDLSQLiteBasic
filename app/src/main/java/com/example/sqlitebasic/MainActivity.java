package com.example.sqlitebasic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private EditText editName, editMsv;
    private ListView lvUser;
    private ArrayAdapter<User> adapter;
    private ArrayList<User> userList = new ArrayList<>();

    int idUpdate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        editName = findViewById(R.id.ed_name);
        editMsv = findViewById(R.id.ed_msv);
        findViewById(R.id.btn_them).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idUpdate < 0) {
                    insertRow();
                }else {
                    updateRow();
                    idUpdate = -1;
                }
                loadData();
            }
        });
        lvUser = findViewById(R.id.list);
        adapter = new ArrayAdapter<User>(this, 0, userList) {

            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.data_item, null);
                TextView tvName = convertView.findViewById(R.id.tvName);
                TextView tvMsv = convertView.findViewById(R.id.tvMsv);
                User u = userList.get(position);
                tvName.setText(u.getName());
                tvMsv.setText(u.getMasv());
                return convertView;
            }
        };
        lvUser.setAdapter(adapter);
        lvUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteUser(position);
                loadData();
                return false;
            }
        });
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInfo(position);
            }
        });
        loadData();
    }
    private void showInfo(int position){
        User u = userList.get(position);
        editName.setText(u.getName());
        editMsv.setText(u.getMasv());

        idUpdate = u.getId();
    }
    private void insertRow() {
        String name = editName.getText().toString();
        String msv = editMsv.getText().toString();
        String sql = "INSERT INTO tbchuno (name, phone) VALUES ('" + name + "','" + msv + "')";
        Toast.makeText(MainActivity.this,"Inserted",Toast.LENGTH_SHORT).show();
        db.execSQL(sql);
    }
    private void updateRow(){
        String name = editName.getText().toString();
        String msv = editMsv.getText().toString();
        String sql = "UPDATE tbchuno SET name = '" + name + "', msv = '" + msv + "' WHERE id = " + idUpdate;
        Toast.makeText(MainActivity.this,"Updated",Toast.LENGTH_SHORT).show();
        db.execSQL(sql);
    }
    private void deleteUser(int position){
        int id = userList.get(position).getId();
        String sql = "DELETE FROM tbchuno WHERE id = " + id;
        Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
        db.execSQL(sql);
    }
    private void initData() {
        db = openOrCreateDatabase("chuno.db", MODE_PRIVATE, null);

        String sql = "CREATE TABLE IF NOT EXISTS tbchuno (id integer primary key autoincrement, name text, phone text)";
        db.execSQL(sql);
    }
    private void loadData() {
        userList.clear();
        String sql = "SELECT * FROM tbchuno";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String msv = cursor.getString(2);
            User u = new User();
            u.setId(id);
            u.setName(name);
            u.setMasv(msv);
            userList.add(u);
            cursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
    }
}
