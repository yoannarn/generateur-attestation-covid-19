package fr.yoannarn.covid19certificategenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class CertificateListAdapter extends RecyclerView.Adapter<CertificateListAdapter.MyViewHolder> {
    private List<File> pdfFiles;
    private Context context;

    public CertificateListAdapter(List<File> pdfFiles) {
        this.pdfFiles = pdfFiles;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public File file;

        public MyViewHolder(View viewItem) {
            super(viewItem);
            this.textView = (TextView) viewItem;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        MyViewHolder viewHolder =  new MyViewHolder(viewItem);

        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        File pdfFile = pdfFiles.get(position);
        holder.file = pdfFile;
        holder.textView.setText(pdfFile.getName());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) view;
                try {
                    CertificateBuilder.openPDF(context, holder.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

}
