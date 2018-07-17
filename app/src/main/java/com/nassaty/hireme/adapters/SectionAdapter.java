package com.nassaty.hireme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Section;
import com.nassaty.hireme.utils.RecyclerViewType;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    private Context context;
    private List<Section> sections;
    private RecyclerViewType recyclerViewType;

    public SectionAdapter(Context context, List<Section> sections, RecyclerViewType recyclerViewType) {
        this.context = context;
        this.sections = sections;
        this.recyclerViewType = recyclerViewType;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_section_content, parent, false);
        return new SectionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        final Section section = sections.get(position);

        //populate
        holder.section_title.setText(section.getSectionLabel());
        holder.item_list.setNestedScrollingEnabled(false);
        holder.item_list.setHasFixedSize(true);

        switch (recyclerViewType){
            case VERTICAL_ORIENTATION:
                LinearLayoutManager layoutVertical = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                holder.item_list.setLayoutManager(layoutVertical);
                break;
            case HORIZONTAL_ORIENTATION:
                LinearLayoutManager layoutHorizontal = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                holder.item_list.setLayoutManager(layoutHorizontal);
                break;
        }

        JobAdapter adapter = new JobAdapter(context, section.getJobs());
        holder.item_list.setAdapter(adapter);

        holder.show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, section.getSectionLabel(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    class SectionViewHolder extends RecyclerView.ViewHolder{

        private TextView section_title;
        private Button show_all;
        private RecyclerView item_list;

        public SectionViewHolder(View itemView) {
            super(itemView);

            section_title = itemView.findViewById(R.id.section_title);
            show_all = itemView.findViewById(R.id.showAll);
            item_list = itemView.findViewById(R.id.item_list);
        }
    }

}
