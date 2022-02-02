package com.example.downsaveinfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

class AnalisadorXml {

   //#####################################################
   //1# variável que irá receber o conteudo XML baixado".
   private String xmlDados;

   //2#Variável que retorna o conteúdo após o processamento do XmlPullParser
   private String conteudo;

   //3# Precisamos criar um construtor para inicializar o objeto dessa classe com o conteudo baixado .

   public AnalisadorXml(String xmlDados) {
      this.xmlDados = xmlDados;
   }
   //4# vamos criar um getter para acessar o conteúdo


   public String getConteudo() {
      return conteudo;
   }

   //5# criando processo para extrair o conteúdo de interesse utilizando a interface XmlPullParser
   public void process() {

      boolean campoValido = false;     //sinaliza se o campo de interesse foi encontrado.
      boolean busca = true;       // sinaliza se a busca deve continuar no laço while

      //6# vamos criar uma estrutura try/catch para o processament
      try {

         // 7.1 Vamos criar uma instancia do tipo XmlPullParserFactory capaz de criar um objeto capaz de tratar o arquivo XML.
         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

         //7.2configura o suporte do factory para espaços reservados a nomes
         factory.setNamespaceAware(true);

         //7.3 Variável xpp que aponta para um objeto que possui metodos que nos permite extrair o conteudo de interesse no XML
         XmlPullParser xpp = factory.newPullParser();

         //7.4 passando o conteudo para o analisador XML.
         xpp.setInput(new StringReader(xmlDados));

         //7.5  vamos criar um marcador para determinar os eventos que irão sendo  encontrados  no código XML.
         // START_DOCUMENT,START_TAG, TEXT, END_TAG, END_DOCUMENT
         int tipoDeEvento;


         //7.6 Laço para busca do conteúdo
         while (busca) {

            //faz o analisador avançar para o próximo evento no XML
            tipoDeEvento = xpp.next();

            //variável para guardar o nome da tag encontrada ( "note", "heading", "body"...)
            String tagName = xpp.getName();

            //7.8 switch para verificar se o campo encontrado é o campo de interesse  e para fazer o tratamento
            switch (tipoDeEvento) {

               // Se é uma tag de inicio de campo...
               case XmlPullParser.START_TAG:

                  //Se a tag for de um campo "body"...
                  if (tagName.equalsIgnoreCase("body")) {
                     // campo válido para leitura do conteúdo de interesse
                     campoValido = true;
                  }
                  break;

               //Se foi encontrado um conteúdo do texto...
               case XmlPullParser.TEXT:
                  //Se for um campo válido par leitura...
                  if(campoValido)
                  {
                     // o texto é armazenado na variável conteudo
                     conteudo=xpp.getText();
                     // é atribuido o valor false para finalização da busca e saida do laço
                     busca=false;
                  }
                  break;

            }
         }
      }
      //8.4 Se ocorrer qualquer tipo de exceção
      catch (Exception e) {
         // status = false;
         e.printStackTrace();
      }

   }


}
