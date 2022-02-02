package com.example.downsaveinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // 1- variável EditText no Java para captura do conteúdo inserido pelo usuário (associada ao componente EditText "editTextXML" no Layout)
    private EditText mensagemInserida;

    // 8- variável Button no Java  associada ao componente Button "downloadXML" no Layout)
    private Button downloadButton;

    // 2- String contendo a mensagem a ser enviada.
    private String mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 3- Associando o variável mensagemInserida do Java com o componente EditText do arquivoXML
        mensagemInserida = findViewById(R.id.editTextXML);

        // 9- Associando o variável mensagemInserida do Java com o componente EditText do arquivoXML
        downloadButton = findViewById(R.id.downloadXML);

        //10 - criando o método onClick associado ao botão para visualiação do Download.
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalisadorXml analisadorXml = new AnalisadorXml(mensagem);
                analisadorXml.process();
                mensagem = analisadorXml.getConteudo();
                  mensagemInserida.setText(mensagem);
            }
        });

        //7 - Criando uma Instância da AsyncTask e executando a tarefa utilizando o método execute().
        MinhaAsync minhaAsync = new MinhaAsync();
        minhaAsync.execute("https://www.w3schools.com/xml/note.xml");

    }
    // 4- criando o método para envido da mensagem para a próxima tela
    public void disparoNovaTela(View v) {

        //4.1 atribuição do valor digitado pelo usuário no campo do EditTexXML para a variável mensagem

        mensagem = mensagemInserida.getText().toString();

        //4.2 Criação da Intent para chamada da segunda tela com envio da mensagem.
        Intent myIntent = new Intent(this, Tela2.class);

        //4.3 Uso do método putExtra para envio da mensagem.
        myIntent.putExtra("mensagemEnviada", mensagem);

        //4.4 Envio da solicitação
        startActivity(myIntent);

    }
    // 5 - Criando o método para realizar o download do conteúdo XML do site, utilizando a classe HttpURLConnection.

    private String downloadXMLFile(String theUrl){
        try {
            //cria um instância da classe URL com a url que será utilizada na conexão.
            URL myUrl = new URL(theUrl);

            //abre a conexão
            HttpURLConnection myconnection = (HttpURLConnection) myUrl.openConnection();

            //verifica se foi bem sucedida a conexão e exibe no Logcat; se sim código 200
            int response = myconnection.getResponseCode();
            Log.d("Download", "The response code is " + response);

            // cria a variável "data" que para receber o stream de bytes
            InputStream data = myconnection.getInputStream();

            //usa a classe InputStreamReader para converter bytes em chars
            InputStreamReader caracteres = new InputStreamReader(data);

            //criação de um array de char para leitura de 500 em 500 caracteres
            char[] inputBuffer = new char[500];

            //Criação de uma instância StringBuilder para formar a String final de interesse
            StringBuilder tempBuffer = new StringBuilder();

            //variável para contagem de número de caracteres lidos dentro do laço while para formação da String
            int charRead;

            //laço de leitura e formação da string
            while (true) {
                //lê os caracteres com tamanho máximo de inputBuffer(500) e informa o número lido
                charRead =caracteres.read(inputBuffer);
                //se -1 não há caracteres lidos e sai do laço.
                if (charRead <= 0) {
                    break;
                }
                tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));

            }
            return tempBuffer.toString();
        }
        //como as os métodos openConnection(), getResponseCode() e getInputStream() podem lançar exceções de IO
        //pois interagem com recursos externos ao aplicativo, precisamos de um bloco cath para capturar eventuais exceções.
        catch(IOException e)
        {
            Log.d("Download", "IO Expection durante a conexão: " + e.getMessage());
        }
        //e nesse caso o método retorna null.
        return null;
    }

    //Precisamos dar permissão de acesso a internet ao aplicativo no arquivo Manifest
    //   <uses-permission android:name="android.permission.INTERNET"></uses-permission>

    //6 - Vamos criar a classe  que é uma AsynkTask,
// que através de uma thread paralela fará a  chamada do método DownloadXMLFile para baixarmos o conteúdo do Feed

    private class MinhaAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            //vamos efocar o método downloadXMLFile passando a url como parâmetro
            mensagem = downloadXMLFile(params[0]);

            //Aviso para ser exibido no Logcat caso tenha havido problema no downlod
            if (mensagem == null) {
                Log.d("Download", "Erro downloading");
            }
            return mensagem;
        }
    }
}