package br.com.ifsul.cadrasto.usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.ifsul.cadrasto.usuarios.domain.Usuario;

import static android.widget.Toast.LENGTH_SHORT;
import static br.com.ifsul.cadrasto.usuarios.domain.Genero.FEMININO;
import static br.com.ifsul.cadrasto.usuarios.domain.Genero.MASCULINO;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;

public class CadastroActivity extends AppCompatActivity implements ValidationListener {

    private static final ArrayList<Usuario> USUARIOS_CADASTRARDOS = new ArrayList<>();
    private static final String EXTRA_LIST = "usuarios_list";

    private TextInputLayout txtNome;
    private TextInputLayout txtEmail;
    private TextInputLayout txtTelefone;
    private TextInputLayout txtDataNascimento;

    @NotEmpty(message = "Campo obrigatório!")
    @Length(min = 3, max = 20, message = "O nome de deve ter entre 3 e 20 caracteres!")
    private TextInputEditText edNome;

    @Email(message = "Email inválido!")
    @NotEmpty(message = "Campo obrigatório!")
    private TextInputEditText edEmail;

    @NotEmpty(message = "Campo obrigatório!")
    @Length(min = 13, max = 13, message = "O telefone deve ter 13 caracteres!")
    private TextInputEditText edTelefone;

    @NotEmpty(message = "Campo obrigatório!")
    @Length(min = 10, max = 10, message = "A data deve ter 10 caracteres!")
    private TextInputEditText edDataNascimento;

    @Checked(message = "Escolha algum gênero")
    private RadioGroup rgGenero;

    private CheckBox cbFilme;
    private CheckBox cbMusica;

    private Button btCadastrar;
    private Button btEnviar;

    private Validator validator;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        incializarComponentes();

        btCadastrar.setOnClickListener(view -> validator.validate());
        btEnviar.setOnClickListener(view -> {
            if (!USUARIOS_CADASTRARDOS.isEmpty()) {
                final Intent menuIntent = new Intent(this, MenuActivity.class);
                menuIntent.putExtra(EXTRA_LIST, USUARIOS_CADASTRARDOS);
                startActivity(menuIntent);
                finish();
            } else {
                Toast.makeText(this, "Cadastre algum usuário primeiro", LENGTH_SHORT).show();
            }
        });
    }

    private void incializarComponentes() {
        txtNome = findViewById(R.id.txt_nome);
        txtEmail = findViewById(R.id.txt_email);
        txtTelefone = findViewById(R.id.txt_telefone);
        txtDataNascimento = findViewById(R.id.txt_data_nascimento);

        edNome = findViewById(R.id.ed_nome);
        edEmail = findViewById(R.id.ed_email);

        edTelefone = findViewById(R.id.ed_telefone);
        final SimpleMaskFormatter maskTelefone = new SimpleMaskFormatter("(NN)NNNNNNNNN");
        final MaskTextWatcher maskTextTelefone = new MaskTextWatcher(edTelefone, maskTelefone);
        edTelefone.addTextChangedListener(maskTextTelefone);


        edDataNascimento = findViewById(R.id.ed_data_nascimento);
        final MaskPattern dezenasDias = new MaskPattern("[0-3]");
        final MaskPattern unidades = new MaskPattern("[0-9]");
        final MaskPattern dezenasMes = new MaskPattern("[0-1]");

        final MaskFormatter maskData = new MaskFormatter("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]");
        maskData.registerPattern(dezenasDias);
        maskData.registerPattern(unidades);
        maskData.registerPattern(dezenasMes);
        final MaskTextWatcher maskTextData = new MaskTextWatcher(edDataNascimento, maskData);
        edDataNascimento.addTextChangedListener(maskTextData);


        rgGenero = findViewById(R.id.rg_genero);

        cbFilme = findViewById(R.id.cb_filme);
        cbMusica = findViewById(R.id.cb_muscia);

        btCadastrar = findViewById(R.id.bt_cadastrar);
        btEnviar = findViewById(R.id.bt_enivar);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        final List<String> intereses = new ArrayList<>();
        final Usuario.UsuarioBuilder usuarioBuilder = Usuario.builder();
        usuarioBuilder.nome(edNome.getText().toString());
        usuarioBuilder.email(edEmail.getText().toString());
        usuarioBuilder.telefone(edTelefone.getText().toString());
        usuarioBuilder.dataNascimento(LocalDate.parse(edDataNascimento.getText().toString(), ofPattern("dd/MM/yyyy")));

        if (cbFilme.isChecked()) {
            intereses.add(cbFilme.getText().toString());
        }
        if (cbMusica.isChecked()) {
            intereses.add(cbMusica.getText().toString());
        }

        usuarioBuilder.interesses(intereses);

        switch (rgGenero.getCheckedRadioButtonId()) {
            case R.id.rb_masculino:
                usuarioBuilder.genero(MASCULINO);
                break;
            case R.id.rb_feminino:
                usuarioBuilder.genero(FEMININO);
                break;
            default:
                Toast.makeText(this, "Algo não esta certo", LENGTH_SHORT).show();
                break;
        }
        final Usuario usuario = usuarioBuilder.build();
        USUARIOS_CADASTRARDOS.add(usuario);
        Toast.makeText(this, format("Toast: %s,\n %s,\n %s,\n %s,\n %s,\n %s",
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                edDataNascimento.getText().toString(),
                usuario.getGenero(),
                usuario.getInteresses().toString()), LENGTH_SHORT).show();

        clearInputs();
    }

    @Override
    public void onValidationFailed(final List<ValidationError> errors) {
        errors.forEach(erro -> {
            final View view = erro.getView();
            final String message = erro.getCollatedErrorMessage(this);

            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputs() {
        edNome.getText().clear();
        edNome.clearFocus();
        edEmail.getText().clear();
        edEmail.clearFocus();
        edTelefone.getText().clear();
        edTelefone.clearFocus();
        edDataNascimento.getText().clear();
        edDataNascimento.clearFocus();

        rgGenero.clearCheck();

        cbMusica.setChecked(false);
        cbFilme.setChecked(false);
    }

}