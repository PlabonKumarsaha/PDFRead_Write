package com.example.pdfconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Document;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PdfWrite extends AppCompatActivity {

    EditText text;
    Button save_btn;
    private static final int STORAGE_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_write);
        text = findViewById(R.id.text);

        save_btn = findViewById(R.id.save_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checking for SDK verion
                if(Build.VERSION.SDK_INT >Build.VERSION_CODES.M){

                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                        //permit was not granted
                        String [] permistion = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permistion,STORAGE_CODE);
                    }else {
                        //permistion grated thus we have to save the pfd
                        savePdf();

                    }

                }else {
                    //system os< marhshmello or low no require to check permit..so direct call to pdf method
                    savePdf();
                }

            }
        });
    }

    private void savePdf() {

        Document document = (Document) new com.itextpdf.text.Document();
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());

        //pdf file path
        String mFilepath = Environment.getExternalStorageDirectory()+"/"+fileName+".pdf";

        try {
            //create file instace of pdfwriter
            PdfWriter.getInstance((com.itextpdf.text.Document) document,new FileOutputStream(mFilepath));
            ((com.itextpdf.text.Document) document).open();

            String mtext = text.getText().toString();

            //add author to the document(optional)
            ((com.itextpdf.text.Document) document).addAuthor("PKS");

            //texts are converted to paragrapgs

            ((com.itextpdf.text.Document) document).add(new Paragraph(mtext));

            //close the isnatnce

            ((com.itextpdf.text.Document) document).close();

            Toast.makeText(getApplicationContext(),fileName+".pdf is"+"aitosaved to"+mFilepath,Toast.LENGTH_SHORT).show();


        } catch (Exception e){

            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

        }


    }

    //handle permistion result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permistion was granted from popup, so call save pdf
                    savePdf();
                }
                else{
                    //permition denied so show error msg
                    Toast.makeText(PdfWrite.this,"Permit denined",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    /*
    1. import the dependency : https://itextpdf.com/en/resources/installation-guides/installing-itext-g-android
    2. take an edit text and button
    3.take run time permit to write external storage


     */
}
