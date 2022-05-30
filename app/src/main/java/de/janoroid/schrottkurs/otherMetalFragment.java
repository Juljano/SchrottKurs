package de.janoroid.schrottkurs;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class otherMetalFragment extends Fragment {

    private Spinner spSchrott;

    ArrayAdapter<CharSequence> adapter;

    private static String[] urls = new String[7];

    private String Price,MarketValue,Name;

    private TextView tvChoosedItem;

    private ProgressBar progress;

    private String[] schrottartenSonstige;
    private String[] PriceArray;
    private String[] PriceTable;
    private String[] PriceRotguß;
    private float sum;

    private List<PriceofMetal> priceofAluminiumList;
    private RecyclerView recyclerView;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messing, container, false);
        getActivity().setTitle("Sonstige Schrottarten");



        //Hier sind alle Links gespeichert,damit sie aufgerufen werden können

        urls[0] = "https://www.ariva.de/zinn_london-kurs/euro";
        urls[1] = "https://www.ariva.de/zink_london-kurs/euro";
        urls[2] = "https://www.ariva.de/blei_london-kurs/euro";
        urls[3] = "https://www.schrott24.de/altmetall-ankauf/legierungen-cu-ni/rotguss-stueckig-max-05-x-05-m/";
        urls[4] = "https://www.schrott24.de/altmetall-ankauf/blei/blei-batterien-aus-kfz-und-maschinen/";
        urls[5] = "https://www.schrott24.de/messingpreise/";
        urls[6] = "http://schrottpreise-info.de";


        //Hier werden die Elemente zugewiesen mit einer ID
        spSchrott = view.findViewById(R.id.spinnerSchrott);

        tvChoosedItem = view.findViewById(R.id.tvItemView);

        progress = view.findViewById(R.id.progressBar);



        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //initializing the productlist
        priceofAluminiumList = new ArrayList<>();


          //Der Array-Adapter für den Spinner

       adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Schrottarten, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Fügt deh Adapter zum den Spinner
        spSchrott.setAdapter(adapter);




      //Die Methode Content wird ausgeführt




            //Hier kann der User ein Schrottart aussuchen und die Informationen bekommen
        spSchrott.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Content content = new Content();

                switch (pos) {

                    case 0:
                        content.execute(urls[0]);
                        tvChoosedItem.setText("Ausgewählt wurde: Zinn");

                        break;

                    case 1:

                        content.execute(urls[1]);
                        tvChoosedItem.setText("Ausgewählt wurde: Zink");

                        break;

                    case 2:
                        content.execute(urls[2]);
                        tvChoosedItem.setText("Ausgewählt wurde: Blei");

                        break;

                    case 3:
                        content.execute(urls[3]);
                        tvChoosedItem.setText("Ausgewählt wurde: Rotguß-Stückig");

                        break;

                    case 4:
                        content.execute(urls[4]);
                        tvChoosedItem.setText("Ausgewählt wurde: Blei-Batterien");

                        break;

                    case 5:
                        content.execute(urls[5]);
                        tvChoosedItem.setText("Ausgewählt wurde: Messing");


                        break;

                    case 6:
                        content.execute(urls[6]);
                        tvChoosedItem.setText("Ausgewählt wurde: Sonstíge Schrottarten");

                        break;

                    default:


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        return view;



    }


    public class Content extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... params) {

   /*
                Es werden die Urls aus den Array gelesen und geparset.
                Der String Webpage speichert die Url,damit die if-Abfrage schauen kann, was geparset werden muss da es unterschiedliche Webseiten sind.
                 */


                try {

                    Document doc = Jsoup.connect(params[0]).get();
                    String Webpage = doc.location();


                    if (Webpage.equals(urls[0]) | Webpage.equals(urls[1]) | Webpage.equals(urls[2])) {


                        Name = doc.select("span[itemprop=\"name\"]").text().substring(20, 25);
                        MarketValue = doc.select("span[itemprop=\"price\"]").text();

                        System.out.println("Sum " + MarketValue);
                        BigDecimal money = new BigDecimal(MarketValue.replaceAll(",", ""));
                        sum = Float.parseFloat(String.valueOf(money));
                        System.out.println(sum/1000*1000*100);


                        if (doc.select(".last.colloss").text().isEmpty())
                        {

                            Price = doc.select(".last.colwin").text();

                        }

                        else {

                            Price = doc.select(".last.colloss").text();
                        }

                        priceofAluminiumList.add(
                                new PriceofMetal(
                                        Name,
                                        "Marktpreis: "+ MarketValue + "€","Kaufpreis: "  + Price,R.drawable.ic_baseline_euro_24));


                        priceofAluminiumList.add(
                                new PriceofMetal(
                                        "Preis pro Kg",
                                        "100kg: " + new DecimalFormat("##.##").format(sum/1000*100*100) + "€", "1kg: " + new DecimalFormat("##.##").format(sum/1000*1*100) + "€",R.drawable.ic_baseline_scale_24));



                    }



                    else if (Webpage.equals(urls[3]))

                    {

                        PriceRotguß = doc.select("span[class=\"singlePrice\"]").text().split(" ");


                        priceofAluminiumList.add(
                                new PriceofMetal(
                                        "Rotguss,stückig",
                                        "ab 500kg: "+ PriceRotguß[2] + "€","ab 100kg: "  + PriceRotguß[1] +"€", R.drawable.ic_baseline_euro_24));



                    }


                    else if (Webpage.equals(urls[4]))


                    {


                       PriceArray = doc.select("span[class=\"singlePrice\"]").text().split(" ");

                        priceofAluminiumList.add(
                                new PriceofMetal(
                                        "Blei-Batterien",
                                        "ab 500kg: "+ PriceArray[2] + "€","ab 300kg: "  + PriceArray[1] +"€", R.drawable.ic_baseline_euro_24));





                    }


                    else if (Webpage.equals(urls[5]))


                    {

                        PriceTable = doc.select("span[class=\"sc-price\"]").text().split(" ");

                        priceofAluminiumList.add(
                                new PriceofMetal(
                                        "Messing MS58, neu, stückig",
                                        "ab 500kg: "+ PriceTable[2] + "€","ab 100kg: "  + PriceTable[1] +"€", R.drawable.ic_baseline_euro_24));

                        priceofAluminiumList.add(
                                new PriceofMetal(
                                        "Messing-Späne, gemischt",
                                        "ab 500kg: "+ PriceTable[5] + "€","ab 100kg: "  + PriceTable[4] +"€", R.drawable.ic_baseline_euro_24));

                        priceofAluminiumList.add(
                                new PriceofMetal(
                                        "Messing, gemischt",
                                        "ab 500kg: "+ PriceTable[8] + "€","ab 100kg: "  + PriceTable[7] +"€", R.drawable.ic_baseline_euro_24));

                        priceofAluminiumList.add(
                                new PriceofMetal(
                                        "Messing-Patronenhülsen",
                                        "ab 500kg: "+ PriceTable[11] + "€","ab 100kg: "  + PriceTable[10] +"€", R.drawable.ic_baseline_euro_24));




                    }


                    else if (Webpage.equals(urls[6]))

                    {

                    schrottartenSonstige = doc.select("div[class=\"mk-fancy-table table-style1\"]").text().substring(60).split("€");


                    priceofAluminiumList.add(
                            new PriceofMetal(
                                    "Sonstige Schrottarten",
                                    "€/kg: "+ schrottartenSonstige[0] + "€","€/kg: "  + schrottartenSonstige[1] +"€", R.drawable.ic_baseline_euro_24));

                    priceofAluminiumList.add(
                            new PriceofMetal(
                                    "Sonstige Schrottarten",
                                    "€/kg: "+ schrottartenSonstige[2] + "€","€/kg: "  + schrottartenSonstige[8] +"€", R.drawable.ic_baseline_euro_24));

                        priceofAluminiumList.add(
                                new PriceofMetal(
                                        "Sonstige Schrottarten",
                                         "-","€/kg: "+ schrottartenSonstige[3] + "€", R.drawable.ic_baseline_euro_24));



                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }




        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);


            progress.setVisibility(View.GONE);

            //creating recyclerview adapter
            ProductAdapter adapter = new ProductAdapter(getActivity(), priceofAluminiumList);

            //setting adapter to recyclerview
            recyclerView.setAdapter(adapter);
        }

    }

}