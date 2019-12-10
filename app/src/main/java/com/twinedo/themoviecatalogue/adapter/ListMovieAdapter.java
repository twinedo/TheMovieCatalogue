package com.twinedo.themoviecatalogue.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.twinedo.themoviecatalogue.R;
import com.twinedo.themoviecatalogue.image.ImageParse;
import com.twinedo.themoviecatalogue.image.ImageSize;
import com.twinedo.themoviecatalogue.object.Movie;

import java.util.ArrayList;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.MoviesViewHolder> {

    private ArrayList<Movie> listMovies = new ArrayList<>();
    private OnClickListener onClickListener;

    public void setListMovies(ArrayList<Movie> listMovies) {
        this.listMovies.clear();
        this.listMovies.addAll(listMovies);
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MoviesViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        Movie movie = listMovies.get(position);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvRelease.setText(movie.getRelease_date());
        holder.tvRate.setText(String.valueOf(movie.getVote_average()));

        if (listMovies.get(position).getPoster_path() == null) {

            Uri noImage = Uri.parse("android.resource://"+ (R.class.getPackage()).getName()+"/"+ R.drawable.noimage);
            holder.loadImage(noImage.toString());
        } else {
            Uri uri2 = ImageParse.movieUrl(ImageSize.w500.getValue(), listMovies.get(position).getPoster_path());
            holder.loadImage(uri2.toString());
        }

        holder.setItemClickListener(onClickListener, listMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvRelease;
        private TextView tvRate;
        private ImageView imgPoster;

        private MoviesViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.movie_title);
            tvRelease = itemView.findViewById(R.id.movie_release);
            tvRate = itemView.findViewById(R.id.movie_rate);
            imgPoster = itemView.findViewById(R.id.movie_poster);

        }

        private void loadImage(String imgUrl){
            Glide.with(itemView).load(imgUrl).centerCrop().into(imgPoster);

        }

        private void setItemClickListener(final OnClickListener onClickListener, final Movie movie) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClickListener(movie.id, movie);
                }
            });
        }
    
    }

    public interface OnClickListener {
        void onItemClickListener(long id, Movie movie);
    }
}
