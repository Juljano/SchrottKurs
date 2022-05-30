package de.janoroid.schrottkurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<PriceofMetal> priceofMetalList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<PriceofMetal> priceofMetalList) {
        this.mCtx = mCtx;
        this.priceofMetalList = priceofMetalList;
    }



    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recyclerview_item_layout, null);
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
         PriceofMetal priceofAluminium = priceofMetalList.get(position);

        //binding the data with the viewholder views
        holder.tvTitle.setText(priceofAluminium.getTitle());
        holder.tvmarktpreis.setText(String.valueOf(priceofAluminium.getMarcetprice()));
        holder.tvkaufpreis.setText(String.valueOf(priceofAluminium.getPrice()));
        holder.imgPicture.setImageResource(priceofAluminium.getImageResource());



    }


    @Override
    public int getItemCount() {
        return priceofMetalList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvmarktpreis, tvkaufpreis;
        ImageView imgPicture;

        public ProductViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvUeberschrift);
            tvmarktpreis = itemView.findViewById(R.id.tvMarktpreis);
            tvkaufpreis = itemView.findViewById(R.id.tvKaufpreis);
            imgPicture = itemView.findViewById(R.id.imageView);
        }
    }
}