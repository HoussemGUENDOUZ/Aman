package com.example.aman;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private Map<String, String> userNames;

    public ProductAdapter(Context context, List<Product> productList, Map<String, String> userNames) {
        this.context = context;
        this.productList = productList;
        this.userNames = userNames;
    }
    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
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
        String userName = userNames.get(product.getStoreKeperID());
       // holder.productName.setText(product.getName());
        //holder.productDescription.setText(product.getDescription());
        holder.M_productDescription.setText("Description : " + product.getDescription());
        holder.M_productDetail.setText("Détails : " + product.getDetails());
        holder.M_ProductName.setText("Produit : " + product.getName());
        holder.phone_number.setText(product.phoneNumber);
        if (userName != null) {
            holder.productDescription.setText(product.getDescription() + " - added by " + userName);
        }
        holder.call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+product.phoneNumber));
//startActivity(intent3);
                context.startActivity(intent3);
            }
        });

        //holder.productImage.setImageResource());
      Picasso.get().load(product.getImage()).into(holder.productImage);

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
        TextView productDescription, productDetail, M_productDescription, M_productDetail,M_ProductName,phone_number;
        RelativeLayout rv;
        ImageView productImage ;
        ImageButton call_btn;
        Button detailBTN;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameTV);
            productDescription = itemView.findViewById(R.id.productDescriptionTV);
            productDetail = itemView.findViewById(R.id.productDetailTV);
            productImage = itemView.findViewById(R.id.productImage);
            rv = itemView.findViewById(R.id.detailRL);
            detailBTN = itemView.findViewById(R.id.detailBTN);

            M_productDescription= itemView.findViewById(R.id.M_ProductDescription);
            M_ProductName= itemView.findViewById(R.id.M_ProductName);
            M_productDetail= itemView.findViewById(R.id.M_ProductDetails);

            call_btn= itemView.findViewById(R.id.call_btn);
            phone_number= itemView.findViewById(R.id.phone_number);


        }
    }
}
