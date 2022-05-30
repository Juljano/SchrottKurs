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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AluminiumFragment extends Fragment {

    private Bitmap bitmap;
    private ImageView GraphImageview;
    private ProgressBar progress;
    private  String PriceEuro;
    private  String PriceDollar;
    private  String QouteDollar;
    private  String QouteEuro;
    private  String Prozent;
    private float sum;


    private List<PriceofMetal> priceofAluminiumList;
    private RecyclerView recyclerView;



    //private TextView qouteDollar, qouteEuro, title, priceeuro, pricedollar, InternetTextview, ProzentTextview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aluminium, container, false);
        getActivity().setTitle("Aluminium");




        Content content = new Content();
        content.execute();


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initializing the productlist
        priceofAluminiumList = new ArrayList<>();

        GraphImageview = (ImageView) view.findViewById(R.id.imgGraph);

        progress = (ProgressBar) view.findViewById(R.id.progressBar);

        return view;

    }


    public class Content extends AsyncTask<Void, Void, Float> {


        @Override
        protected void onPreExecute() {



            super.onPreExecute();
        }

        @Override
        protected Float doInBackground(Void... voids) {





            String[] urls = new String[]{"https://www.ariva.de/aluminium_london-kurs/euro", "https://www.ariva.de/aluminium_london-kurs"};

            SchrottPreisEuro(urls[0]);
            SchrottPreisDollar(urls[1]);


            return null;
        }



        @Override
        protected void onPostExecute(Float aVoid) {
            super.onPostExecute(aVoid);




            priceofAluminiumList.add(
                    new PriceofMetal(
                            "Aluminium-Preis in €",
                            "Marktpreis "+ QouteEuro + "€","Kaufpreis: " + PriceEuro,R.drawable.ic_baseline_euro_24));


            String prozenttext = "Der Aktuelle Kurs beträgt: ";
            priceofAluminiumList.add(
                    new PriceofMetal(
                            "Aluminium-Kurs in %",
                             Prozent, prozenttext,R.drawable.ic_baseline_percent_24));

            priceofAluminiumList.add(
                    new PriceofMetal(
                            "Preis pro Kg",
                            "100kg: " + new DecimalFormat("##.##").format(sum/1000*100*100) + "€", "1kg: " + new DecimalFormat("##.##").format(sum/1000*1*100) + "€",R.drawable.ic_baseline_scale_24));


            progress.setVisibility(View.GONE);




            //creating recyclerview adapter
            ProductAdapter adapter = new ProductAdapter(getActivity(), priceofAluminiumList);

            //setting adapter to recyclerview
            recyclerView.setAdapter(adapter);



            GraphImageview.setImageBitmap(bitmap);
            GraphImageview.getLayoutParams().width = 1100;
            GraphImageview.getLayoutParams().height = 600;



        }


        public void SchrottPreisEuro(String url) {
            try {


                Document doc = Jsoup.connect(url).get();


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


                QouteEuro = doc.select("span[itemprop=\"price\"]").text(); //10.260,33

                System.out.println("Sum " + QouteEuro);
                BigDecimal money = new BigDecimal(QouteEuro.replaceAll(",", ""));
                sum = Float.parseFloat(String.valueOf(money));
                System.out.println(sum/1000*1000*100);


                InputStream input = new java.net.URL("https://www.ariva.de/chart/images/chart.png?z=a101622751~b172~dEUR~o285~wfree~Uyear~W0~z620").openStream();
                bitmap = BitmapFactory.decodeStream(input);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        public void SchrottPreisDollar(String url) {
            try {

                Document doc = Jsoup.connect(url).get();

              //  Title = doc.title();

                QouteDollar = doc.select("span[itemprop=\"price\"]").text(); //11.424,78


                if (doc.select(".last.colloss").text().isEmpty()){
                    PriceDollar = doc.select(".last.colwin").text(); //+176,0953 €

                }else{
                    PriceDollar = doc.select(".last.colloss").text(); //+176,0953 €

                }




            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



}





