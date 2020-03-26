package com.example.jokenpo_udemy;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.support.v7.app.AlertDialog;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;
import java.text.MessageFormat;
import java.util.Random;


public class MainActivity extends Activity {
    ImageButton btnPapel;
    MediaPlayer media;
    ImageButton btnPedra;
    ImageButton btnTesoura;
    ImageView imgJogador1;
    ImageView imgJogador2;
    AlertDialog.Builder alerta;
    Animation sumir;
    TextView txtContador;
    Animation aparecer;
    int jogada1;
    int jogada2;
    int vitoria;
    boolean estado;
    SharedPreferences prefJogador;
    SharedPreferences.Editor editor;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        media = MediaPlayer.create(MainActivity.this, R.raw.alex_play);
        imgJogador1 = findViewById(R.id.imgJogador1);
        imgJogador2 = findViewById(R.id.imgJogador2);
        btnPedra = findViewById(R.id.btnPedra);
        btnPapel = findViewById(R.id.btnPapel);
        btnTesoura = findViewById(R.id.btnTesoura);
        txtContador =  findViewById(R.id.txtContador);
        alerta = new AlertDialog.Builder(this);
        sumir = new AlphaAnimation(1,0);
        aparecer = new AlphaAnimation(0,1);
        sumir.setDuration(1500);
        aparecer.setDuration(100);
        prefJogador = getSharedPreferences("prefJogador",MODE_PRIVATE);
        txtContador.setText(MessageFormat.format("{0}",prefJogador.getInt("vitoria",0)));

        aparecer.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationStart(Animation animation){
                jogada();
                imgJogador2.setVisibility(View.INVISIBLE);
            }
            public void onAnimationEnd(Animation animation){
                resultado();
                estado = true;
                imgJogador2.setVisibility(View.VISIBLE);
            }
            public void onAnimationRepeat(Animation animation){
            }
        });
        sumir.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationStart(Animation animation){
                imgJogador2.setVisibility(View.VISIBLE);
            }
            public void onAnimationEnd(Animation animation){
                imgJogador2.setVisibility(View.INVISIBLE);
                imgJogador2.startAnimation(aparecer);
            }
            public void onAnimationRepeat(Animation animation){
            }
        });
    }
    public void clickJogador(View view){
        tocasom();

        imgJogador1.setScaleX(-1f);
        switch(view.getId()){
            case R.id.btnPedra: imgJogador1.setImageResource(R.drawable.pedra);jogada1=1;break;
            case R.id.btnPapel: imgJogador1.setImageResource(R.drawable.papel);jogada1=2;break;
            case R.id.btnTesoura: imgJogador1.setImageResource(R.drawable.tesoura);jogada1=3;break;



        }
        imgJogador2.setImageResource(R.drawable.interrogacao);
        imgJogador2.startAnimation(sumir);
    }
    public void jogada(){
        Random r = new Random();
        int result = r.nextInt(3);

        switch (result){
            case 0: imgJogador2.setImageResource(R.drawable.pedra);jogada2=1;break;
            case 1: imgJogador2.setImageResource(R.drawable.papel);jogada2=2;break;
            case 2 : imgJogador2.setImageResource(R.drawable.tesoura);jogada2=3;break;
        }
    }
    public void resultado(){
        if(jogada1 == jogada2){

            Toast.makeText(this, "Empatou!!", Toast.LENGTH_SHORT).show();
            alerta.setTitle("JokemPo!!!").setMessage("JOGO EMPATADO!!!").setPositiveButton("Ok",null).show();
        }
        else if ((jogada1 == 1 && jogada2==3)||(jogada1 == 2 && jogada2==1)||(jogada1 == 3 && jogada2 == 2)) {
            alerta.setTitle("JokemPo!!!").setMessage("VOCÊ GANHOU!!!").setPositiveButton("Ok",null).show();
            Toast.makeText(this, "Ganhou!!", Toast.LENGTH_SHORT).show();
            gravarResult();
        }
        else{
            Toast.makeText(this, "Perdeu!!", Toast.LENGTH_SHORT).show();
            alerta.setTitle("JokemPo!!!").setMessage("VOCÊ PERDEU!!!").setPositiveButton("Ok",null).show();
        }
    }
    public void tocasom(){
       if(media != null){
           media.start();
       }
    }
    protected void onPause() {
        super.onPause();
            media.pause();
        Log.i("logx","teste onPause: "+String.valueOf(estado));

    }
    protected void onResume() {
        super.onResume();
        if(estado == true){
            media.start();
            estado = false;
        }
        Log.i("logx","Teste onResume: "+String.valueOf(estado));

    }
    public void gravarResult(){
        int vitoria;
        vitoria = prefJogador.getInt("vitoria",0);
        vitoria +=1;
        editor = prefJogador.edit();
        editor.putInt("vitoria",vitoria);
        editor.apply();
        txtContador.setText(MessageFormat.format("{0}", prefJogador.getInt("vitoria",0)));
    }
}
