package com.mel7em.customlisteviewexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<Product> arrayList;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        arrayList=new ArrayList<> ();
        lv=findViewById (R.id.ls);
        runOnUiThread (new Runnable () {
            @Override
            public void run() {
                new ReadJSON ().execute ("http://10.75.25.127:8081/commerce/all.php");

            }
        });
    }
    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }
        @Override
        protected void onPostExecute(String content){

            try {
                JSONObject jsonObject=new JSONObject (content);
                JSONArray jsonArray=jsonObject.getJSONArray ("results");
                for(int i=0;i<jsonArray.length ();i++){
                    JSONObject productObject= jsonArray.getJSONObject (i);
                    arrayList.add (new Product(
                            productObject.getString ("image"),
                    productObject.getString ("name"),
                    productObject.getString ("price")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace ();
            }
            CustomListAdapter adapter=new CustomListAdapter (
                    getApplicationContext (),
                    R.layout.custom_list_layout,
                    arrayList
            );
            lv.setAdapter (adapter);
            lv.setOnItemClickListener (new AdapterView.OnItemClickListener () {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Product product= (Product)lv.getAdapter ().getItem (position);
                    Toast.makeText (MainActivity.this,product.getName ()+" coûte "+product.getPrice ()+"€",Toast.LENGTH_SHORT).show ();
                 AlertDialog.Builder alt = new AlertDialog.Builder (MainActivity.this);
                    alt.setTitle ("Bonjour Maitre!");
                    alt.setMessage ("Que voulez-vous que je fasse?");
                    alt.setPositiveButton ("Delete article", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            delete(product);

                        }
                    });
                    alt.setNegativeButton ("Update article", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          Intent exp = new Intent (getApplicationContext(), UpdateActivity.class);
                            startActivity (exp);
                        }
                    });
                    alt.setNeutralButton("Cancel", null);

                    alt.show ();

                }
            });
        }
    }

    private void delete(Product obj) {

    }


    static String readURL(String theUrl) {
        StringBuilder content= new StringBuilder ();
        try{
            URL url= new URL (theUrl);
            URLConnection urlConnection=url.openConnection ();
            BufferedReader bufferedReader=new BufferedReader (new InputStreamReader (url.openStream ()));
            String line;
            while ((line=bufferedReader.readLine()) !=null){
                content.append(line+"\n");
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace ();
        }

        return content.toString ();
    }
}

     /*   ls=findViewById (R.id.ls);
    }
    private void jsonParseAll() {

       RequestQueue queue= Volley.newRequestQueue (this);


        String url="http://192.168.43.94/commerce/all.php";

        JsonObjectRequest request=new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {
                int success = 0;
                String message = "Echec";
                JSONArray jsonArray= null;
                try {

                    JSONObject reponse=response.getJSONObject ("response");
                    success=reponse.getInt ("succes");
                    message=reponse.getString ("message");


                } catch (JSONException e) {
                    e.printStackTrace ();
                }
                if (success==0){alert(success,message);}
                else {

                    try {
                        jsonArray=response.getJSONArray ("results");
                        ArrayList<Product> values= new ArrayList<> ();

                        for (int i=0;i<jsonArray.length ();i++){

                            JSONObject productObject=jsonArray.getJSONObject (i);
                            values.add (new Product (
                                    productObject.getString ("image"),
                                    productObject.getString ("name"),
                                    productObject.getString ("price")
                            ));
                           /* String name=employee.getString ("name");
                            int age=employee.getInt ("age");
                            HashMap<String,String> map=new HashMap<String, String> ();
                            map.put("name",name);
                            map.put("age",String.valueOf (age));//food.put("imgname",url.concat(jsonObj1.getString("imagename")));
                            // map.put ("img", String.valueOf ("http://192.168.43.94/images/1.jpg"));//Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                            values.add (map);*/
                     /*   }
                        CustomListAdapter adapter =new CustomListAdapter (getApplicationContext (),  R.layout.custom_list_layout,values);
                        ls.setAdapter (adapter);

                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }

                }

            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace ();
            }
        });
        queue.add (request);
    }
    public void alert(int success, String message){
        androidx.appcompat.app.AlertDialog.Builder alt = new AlertDialog.Builder (getApplicationContext ());
        alt.setTitle (message);
        if(success==1)  alt.setMessage ("Voulez-vous inserer d'autres users?");
        else alt.setMessage ("Veuillez corriger votre saisie");
        alt.setPositiveButton ("oui", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alt.setNegativeButton ("non", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //  Intent exp = new Intent (getApplicationContext (), CrudActivity.class);
               // startActivity (exp);
            }
        });
        alt.show ();
    }
}*/