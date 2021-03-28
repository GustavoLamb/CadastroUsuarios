package br.com.ifsul.cadastro.usuarios;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.ifsul.cadastro.usuarios.domain.Usuario;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;

public class ToastActivity extends AppCompatActivity {

    private static final String EXTRA_USUARIO = "usuario";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
        final Usuario usuario = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);

        Toast.makeText(this, format("Toast: %s,\n %s,\n %s,\n %s,\n %s,\n %s",
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getDataNascimento().format(ofPattern("dd/MM/yyyy")),
                usuario.getGenero(),
                usuario.getInteresses().toString()), LENGTH_SHORT).show();
    }
}