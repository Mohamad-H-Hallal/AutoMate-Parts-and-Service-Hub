package com.example.project.View.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.project.Controller.PartController;
import com.example.project.Model.PartModel;
import com.example.project.R;
import com.example.project.View.Adapters.PartsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartsFragment extends BaseFragment {

    private androidx.appcompat.widget.SearchView searchView;
    private ImageView partsFilter;
    private CardView partsCardFilter;
    private Spinner partsMakeSpinner,partsModelSpinner,partsYearSpinner,partsCategorySpinner,partsSubCategorySpinner,partsConditionSpinner;
    private CheckBox partsNegotiable,partsLocation;
    private EditText partsPriceFromEditText,partsPriceToEditText;
    private AppCompatButton partsFilterSubmit;
    private ListView partsListView;

    public PartsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.partsSearch);
        partsFilter = view.findViewById(R.id.partsFilter);
        partsCardFilter = view.findViewById(R.id.partsCardFilter);
        partsMakeSpinner = view.findViewById(R.id.partsMakeSpinner);
        partsModelSpinner =view.findViewById(R.id.partsModelSpinner);
        partsYearSpinner = view.findViewById(R.id.partsYearSpinner);
        partsCategorySpinner = view.findViewById(R.id.partsCategorySpinner);
        partsSubCategorySpinner = view.findViewById(R.id.partsSubCategorySpinner);
        partsConditionSpinner = view.findViewById(R.id.partsConditionSpinner);
        partsNegotiable =view.findViewById(R.id.partsNegotiable);
        partsLocation = view.findViewById(R.id.partsLocation);
        partsPriceFromEditText = view.findViewById(R.id.partsPriceFromEditText);
        partsPriceToEditText = view.findViewById(R.id.partsPriceToEditText);
        partsListView = view.findViewById(R.id.partsListView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
                return false;
            }
        });
        fill_filter();
        partsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(partsCardFilter.getVisibility() == View.VISIBLE){
                    partsCardFilter.setVisibility(View.GONE);
                    partsFilter.setBackground(null);
                }else {
                    partsCardFilter.setVisibility(View.VISIBLE);
                    partsFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
                }
            }
        });

        PartController partc = new PartController();
        partc.fetchParts(requireContext(),new PartController.PartFetchListener() {

     @Override
    public void onPartsFetched(List<PartModel> parts, ArrayList<String> image_path) {
         PartsAdapter adapter = new PartsAdapter(requireContext(),parts,image_path);
         partsListView.setAdapter(adapter);
    }

    @Override
    public void onError(String error) {
        Log.d("error",error);
    }
});



    }


    public void fill_filter(){

        Map<String, List<String>> categorySubcategoryMap = new HashMap<>();
        Map<String, List<String>> makeModelMap = new HashMap<>();
        String[] categoriesArray = getResources().getStringArray(R.array.categories_choices);
        String[] years = getResources().getStringArray(R.array.year_choices);
        String[] subcategoriesArray = getResources().getStringArray(R.array.subcategories_choices);
        String[] makeArray = getResources().getStringArray(R.array.make_choices);
        String[] modelArray = getResources().getStringArray(R.array.model_choices);
        String[] conditionArray = getResources().getStringArray(R.array.condition_choices);

        for (int i = 0; i < categoriesArray.length; i++) {
            String category = categoriesArray[i];
            String[] subcategories = subcategoriesArray[i].split(";");
            categorySubcategoryMap.put(category, Arrays.asList(subcategories));
        }

        for (int i = 0; i < makeArray.length; i++) {
            String make = makeArray[i];
            String[] model = modelArray[i].split(";");
            makeModelMap.put(make, Arrays.asList(model));
        }

        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, conditionArray);
        partsConditionSpinner.setAdapter(conditionAdapter);
        ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, makeArray);
        partsMakeSpinner.setAdapter(makeAdapter);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        partsYearSpinner.setAdapter(yearAdapter);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoriesArray);
        partsCategorySpinner.setAdapter(categoryAdapter);

        partsMakeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMake = (String) parent.getItemAtPosition(position);
                List<String> model = makeModelMap.get(selectedMake);


                ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, model);
                partsModelSpinner.setAdapter(modelAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        partsCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                List<String> subcategories = categorySubcategoryMap.get(selectedCategory);

                // Populate the subcategorySpinner with subcategories for the selected category
                ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subcategories);
                partsSubCategorySpinner.setAdapter(subcategoryAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });
    }
}