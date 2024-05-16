package com.example.project.View.Fragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.text.InputFilter;
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
import android.widget.Toast;

import com.example.project.Controller.PartController;
import com.example.project.Controller.TwoDecimalPlacesInputFilter;
import com.example.project.Controller.UserData;
import com.example.project.Model.PartModel;
import com.example.project.R;
import com.example.project.View.Activities.AddPartsActivity;
import com.example.project.View.Adapters.PartsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PartsFragment extends BaseFragment {

    private androidx.appcompat.widget.SearchView searchView;
    private ImageView partsFilter;
    private CardView partsCardFilter;
    private Spinner partsMakeSpinner, partsModelSpinner, partsYearSpinner, partsCategorySpinner, partsSubCategorySpinner, partsConditionSpinner;
    private CheckBox partsNegotiable, partsLocation;
    private EditText partsPriceFromEditText, partsPriceToEditText;
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
        partsModelSpinner = view.findViewById(R.id.partsModelSpinner);
        partsYearSpinner = view.findViewById(R.id.partsYearSpinner);
        partsCategorySpinner = view.findViewById(R.id.partsCategorySpinner);
        partsSubCategorySpinner = view.findViewById(R.id.partsSubCategorySpinner);
        partsConditionSpinner = view.findViewById(R.id.partsConditionSpinner);
        partsNegotiable = view.findViewById(R.id.partsNegotiable);
        partsLocation = view.findViewById(R.id.partsLocation);
        partsPriceFromEditText = view.findViewById(R.id.partsPriceFromEditText);
        partsPriceToEditText = view.findViewById(R.id.partsPriceToEditText);
        TwoDecimalPlacesInputFilter filter = new TwoDecimalPlacesInputFilter();
        partsPriceFromEditText.setFilters(new InputFilter[]{filter});
        partsPriceToEditText.setFilters(new InputFilter[]{filter});
        partsListView = view.findViewById(R.id.partsListView);
        partsFilterSubmit = view.findViewById(R.id.partsFilterSubmit);

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
                if (partsCardFilter.getVisibility() == View.VISIBLE) {
                    partsCardFilter.setVisibility(View.GONE);
                    partsFilter.setBackground(null);
                } else {
                    partsCardFilter.setVisibility(View.VISIBLE);
                    partsFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
                }
            }
        });

        PartController partc = new PartController();
        partc.fetchParts(requireContext(), new PartController.PartFetchListener() {

            @Override
            public void onPartsFetched(List<PartModel> parts, ArrayList<String> image_path) {
                PartsAdapter adapter = new PartsAdapter(requireContext(), parts, image_path);
                partsListView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
                Log.d("error", error);
            }
        });

        partsFilterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });


    }

    // EnglishContextWrapper class definition
    public static class EnglishContextWrapper extends ContextWrapper {
        public EnglishContextWrapper(Context base) {
            super(base);
        }

        @Override
        public Resources getResources() {
            Configuration configuration = new Configuration(super.getResources().getConfiguration());
            configuration.setLocale(new Locale("en"));
            return createConfigurationContext(configuration).getResources();
        }
    }

    private void applyFilter() {
        Context englishContext = new AddPartsActivity.EnglishContextWrapper(getContext());

        String subcategoriesArray = englishContext.getResources().getStringArray(R.array.subcategories_choices)[partsCategorySpinner.getSelectedItemPosition()];
        String[] subcategoryChoices = subcategoriesArray.split(";");

        String modelArray = englishContext.getResources().getStringArray(R.array.model_choices)[partsMakeSpinner.getSelectedItemPosition()];
        String[] modelChoices = modelArray.split(";");

        String make = partsMakeSpinner.getSelectedItem().toString().equals(partsMakeSpinner.getItemAtPosition(0).toString()) ? "" : englishContext.getResources().getStringArray(R.array.make_choices)[partsMakeSpinner.getSelectedItemPosition()];
        String model = partsModelSpinner.getSelectedItem().toString().equals(partsModelSpinner.getItemAtPosition(0).toString()) ? "" : modelChoices[partsModelSpinner.getSelectedItemPosition()];
        String year = partsYearSpinner.getSelectedItem().toString().equals(partsYearSpinner.getItemAtPosition(0).toString()) ? "" : englishContext.getResources().getStringArray(R.array.year_choices)[partsYearSpinner.getSelectedItemPosition()];
        String category = partsCategorySpinner.getSelectedItem().toString().equals(partsCategorySpinner.getItemAtPosition(0).toString()) ? "" : englishContext.getResources().getStringArray(R.array.categories_choices)[partsCategorySpinner.getSelectedItemPosition()];
        String subcategory = partsSubCategorySpinner.getSelectedItem().toString().equals(partsSubCategorySpinner.getItemAtPosition(0).toString()) ? "" : subcategoryChoices[partsSubCategorySpinner.getSelectedItemPosition()];
        String condition = partsConditionSpinner.getSelectedItem().toString().equals(partsConditionSpinner.getItemAtPosition(0).toString()) ? "" : englishContext.getResources().getStringArray(R.array.condition_choices)[partsConditionSpinner.getSelectedItemPosition()];

        String negotiable, filter_location;
        if (partsNegotiable.isChecked()) {
            negotiable = "true";
        } else {
            negotiable = "false";
        }
        if (partsLocation.isChecked()) {
            filter_location = "true";
        } else {
            filter_location = "false";
        }
        String minPrice = partsPriceFromEditText.getText().toString();
        String maxPrice = partsPriceToEditText.getText().toString();

        PartController partcontroller = new PartController();
        partcontroller.getFilteredParts(requireContext(), UserData.getId(), make, model, year, category, subcategory, condition, negotiable, filter_location, minPrice, maxPrice, new PartController.PartResponseListener() {
            @Override
            public void onSuccess(List<PartModel> parts, ArrayList<String> image_path) {
                PartsAdapter adapter = new PartsAdapter(requireContext(), parts, image_path);
                partsListView.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                // Handle error response
                Log.d("error", message);
            }
        });
    }


    public void fill_filter() {

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