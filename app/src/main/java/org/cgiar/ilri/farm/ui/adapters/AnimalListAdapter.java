package org.cgiar.ilri.farm.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cgiar.ilri.farm.R;
import org.cgiar.ilri.farm.activities.AnimalActivity;
import org.cgiar.ilri.farm.data.realm.objects.Animal;
import org.cgiar.ilri.farm.data.realm.objects.Breed;
import org.cgiar.ilri.farm.data.realm.objects.Sex;
import org.cgiar.ilri.farm.data.realm.objects.Species;
import org.cgiar.ilri.farm.data.realm.utils.RealmDatabase;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by jrogena on 27/02/2016.
 */
public class AnimalListAdapter extends RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder> {
    private static final String TAG = "ILRI.AnimalListAdapter";
    private final ArrayList<String> animalIds;//the order in which the animal ids are in this list is the same order the animal ids will be ordered in the UI
    private final Context context;
    public AnimalListAdapter(Context context) {
        this.context = context;
        this.animalIds = new ArrayList<>();
    }

    /**
     * This method is used to add the provided animal
     *
     * @param animal The animal object to be added
     * @return  Index of the animal in the list
     */
    public int add(Animal animal) {
        if(animal != null) {
            if(!animalIds.contains(animal.getAnimalId())) {
                animalIds.add(animal.getAnimalId());
            }
            return animalIds.indexOf(animal.getAnimalId());
        }
        return -1;
    }

    /**
     * This method is used to add all the specified animals
     *
     * @param animals   List of animals to be added
     * @return  Indexes of the added animals
     */
    public ArrayList<Integer> addAll(RealmResults<Animal> animals) {
        ArrayList<Integer> results = new ArrayList<>();
        if(animals != null) {
            for(int i = 0; i < animals.size(); i++) {
                int currIndex = add(animals.get(i));
                results.add(currIndex);
            }
        }
        return results;
    }

    /**
     * This method is used to clear all the items in the list
     */
    public void clear() {
        int size = animalIds.size();
        animalIds.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public AnimalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_animal_item, parent, false);
        AnimalViewHolder viewHolder = new AnimalViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AnimalViewHolder holder, int position) {
        if(animalIds.size() > position) {
            String animalId = animalIds.get(position);
            holder.refreshData(animalId);
        }
    }

    @Override
    public int getItemCount() {
        return animalIds.size();
    }

    /**
     * This class holds the ViewHolder that works with AnimalListAdapter
     */
    public static class AnimalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView animalIdTV, sexTV, breedTV, speciesTV, statusTV;
        private final View itemView;
        private String animalId;
        private LinearLayout itemCanvasLL;

        public AnimalViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemCanvasLL = (LinearLayout)itemView.findViewById(R.id.item_canvas_ll);
            itemCanvasLL.setOnClickListener(this);
            animalIdTV = (TextView)itemView.findViewById(R.id.animal_id_tv);
            sexTV = (TextView)itemView.findViewById(R.id.sex_tv);
            breedTV = (TextView)itemView.findViewById(R.id.breed_tv);
            speciesTV = (TextView)itemView.findViewById(R.id.species_tv);
            statusTV = (TextView)itemView.findViewById(R.id.status_tv);
        }

        /**
         * This method is used to update the view using the provided realm object
         *
         * @param animalId  The animal Id to be used to fetch the data
         */
        public void refreshData(String animalId) {
            this.animalId = null;
            Realm realm = RealmDatabase.create(itemView.getContext(), null);
            RealmResults<Animal> animals = Animal.getAnimals(realm, animalId);
            if(animals != null && animals.size() == 1) {
                this.animalId = animalId;
                Animal animal = animals.get(0);
                animalIdTV.setText(animalId);
                Context context = itemView.getContext();
                Sex sex = animal.getSex();
                if(sex != null) {
                    sexTV.setText(sex.getName());
                } else  {
                    sexTV.setText(context.getString(R.string.unknown)+" "+context.getString(R.string.sex).toLowerCase());
                }

                Species species = animal.getSpecies();
                if(species != null) {
                    speciesTV.setText(species.getName());
                } else  {
                    sexTV.setText(context.getString(R.string.unknown)+" "+context.getString(R.string.species).toLowerCase());
                }

                RealmList<Breed> breeds = animal.getBreeds();
                if(breeds != null && breeds.size() > 0) {
                    String breedText = "";
                    for(int i = 0; i < breeds.size(); i++) {
                        if(breedText.length() != 0) {
                            breedText = breedText + ", ";
                        }
                        breedText = breedText + breeds.get(i).getName();
                    }
                    breedTV.setText(breedText);
                } else  {
                    breedTV.setText(context.getString(R.string.unknown) + " " + context.getString(R.string.breed).toLowerCase());
                }

                statusTV.setText(animal.getStatus());
            }
            RealmDatabase.close(realm);
        }

        @Override
        public void onClick(View v) {
            if (v.equals(itemCanvasLL)) {
                //start the animal activity
                if(animalId != null) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, AnimalActivity.class);
                    intent.putExtra(AnimalActivity.KEY_ANIMAL_ID, animalId);
                    context.startActivity(intent);
                }
            }
        }
    }
}
