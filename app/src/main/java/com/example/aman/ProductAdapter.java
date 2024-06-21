package com.example.aman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
      //  holder.productImage.setImageResource());
      // Glide.with(context).load(product.getImage()).into(holder.productImage);

        holder.detailBTN.setOnClickListener(v -> {
            if (holder.rv.getVisibility() == View.GONE) {
                holder.rv.setVisibility(View.VISIBLE);
            } else {
                holder.rv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productDescription, productDetail;
        RelativeLayout rv;
        ImageView productImage ;
        Button detailBTN;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameTV);
            productDescription = itemView.findViewById(R.id.productDescriptionTV);
            productDetail = itemView.findViewById(R.id.productDetailTV);
            productImage = itemView.findViewById(R.id.productImage);
            rv = itemView.findViewById(R.id.detailRL);
            detailBTN = itemView.findViewById(R.id.detailBTN);
        }
    }
}
