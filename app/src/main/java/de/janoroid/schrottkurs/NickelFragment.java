package de.janoroid.schrottkurs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class NickelFragment extends Fragment {

    private Bitmap bitmap;
    private ImageView GraphImageview;
    private ProgressBar progress;
    private String PriceEuro;
    private String PriceDollar;
    private String QouteDollar;
    private String QouteEuro;
    private String Prozent;
    private float sum;

    private List<PriceofMetal> priceofAluminiumList;
    private RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nickel, container, false);
        getActivity().setTitle("Nickel");



        //Asyntask wird ausgeführt
        Content content = new Content();
        content.execute();

        //Imageview für den Graphen
        GraphImageview = (ImageView) view.findViewById(R.id.imgGraph);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progress = (ProgressBar) view.findViewById(R.id.progressBar);

        //initializing the productlist
        priceofAluminiumList = new ArrayList<>();

        return view;
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String[] urls = new String[]{"https://www.ariva.de/nickel_london-kurs/euro", "https://www.ariva.de/nickel_london-kurs"};

            SchrottPreisEuro(urls[0]);

            SchrottPreisDollar(urls[1]);

            return null;
        }

                        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

                            GraphImageview.setImageBitmap(bitmap);
                            GraphImageview.getLayoutParams().width = 1100;
                            GraphImageview.getLayoutParams().height = 600;


                            priceofAluminiumList.add(
                                    new PriceofMetal(
                                            "Nickel-Preis in €",
                                            "Marktpreis "+ QouteEuro + "€","Kaufpreis: " + PriceEuro,R.drawable.ic_baseline_euro_24));


                            priceofAluminiumList.add(
                                    new PriceofMetal(
                                            "Nickel-Preis in $",
                                            "Martkpreis: " + QouteDollar + "$", "Kaufpreis: " + PriceDollar,R.drawable.ic_attach_money_white_24dp));


                            String prozenttext = "Der Aktuelle Kurs beträgt: ";
                            priceofAluminiumList.add(
                                    new PriceofMetal(
                                            "Preis pro Kg",
                                            "100kg: " + new DecimalFormat("##.##").format(sum/1000*100*100) + "€", "1kg: " + new DecimalFormat("##.##").format(sum/1000*1*100) + "€",R.drawable.ic_baseline_scale_24));



                            progress.setVisibility(View.GONE);



                            //creating recyclerview adapter
                            ProductAdapter adapter = new ProductAdapter(getActivity(), priceofAluminiumList);

                            //setting adapter to recyclerview
                            recyclerView.setAdapter(adapter);





        }

        private void SchrottPreisEuro(String url){
            try {


                Document doc = Jsoup.connect(url).get();


                QouteEuro = doc.select("span[itemprop=\"price\"]").text(); //10.260,33
                System.out.println("Sum " + QouteEuro);
                BigDecimal money = new BigDecimal(QouteEuro.replaceAll(",", ""));
                sum = Float.parseFloat(String.valueOf(money));
                System.out.println(sum/1000*1000*100);


                if (doc.select(".last.colloss").text().isEmpty()){
                    PriceEuro = doc.select(".last.colwin").text(); //+176,0953 €

                }else{
                    PriceEuro = doc.select(".last.colloss").text(); //+176,0953 €

                }

                if (doc.select("td[class=\"colloss\"]").text().isEmpty()){
                    Prozent = doc.select("td.colwin").first().text();
                }else{
                    Prozent = doc.select("td[class=\"colloss\"]").text();

                }



                InputStream input = new java.net.URL("https://www.ariva.de/chart/images/chart.png?z=a101622756~b172~dEUR~o285~wfree~Uyear~W0~z620").openStream();
                bitmap = BitmapFactory.decodeStream(input);



            } catch (Error | IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Es ist ein Fehler aufgetreten", Toast.LENGTH_SHORT).show();
            }

        }


        private void SchrottPreisDollar(String url){

            try {

                Document doc = Jsoup.connect(url).get();
                QouteDollar = doc.select("span[itemprop=\"price\"]").text(); //11.424,78


                if (doc.select(".last.colloss").text().isEmpty()){
                    PriceDollar = doc.select(".last.colwin").text(); //+176,0953 €

                }else{
                    PriceDollar = doc.select(".last.colloss").text(); //+176,0953 €

                }













            } catch (IOException | Error e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Es ist ein Fehler aufgetreten", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
