package br.com.ifsul.cadrasto.usuarios;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import br.com.ifsul.cadrasto.usuarios.domain.Usuario;

public class MenuActivity extends ListActivity {

    private static final String EXTRA_LIST = "usuarios_list";
    private static final String EXTRA_USUARIO = "usuario";
    private ArrayList<Usuario> usuarioList;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuarioList = (ArrayList<Usuario>) getIntent().getSerializableExtra(EXTRA_LIST);
        final ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usuarioList);
        super.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        super.onListItemClick(l, v, position, id);
        final Intent toastIntent = new Intent(this, ToastActivity.class);
        toastIntent.putExtra(EXTRA_USUARIO, usuarioList.get(position));
        startActivity(toastIntent);
        finish();
    }
}
