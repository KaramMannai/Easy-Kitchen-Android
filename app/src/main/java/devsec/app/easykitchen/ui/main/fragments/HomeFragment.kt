package devsec.app.easykitchen.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import devsec.app.easykitchen.R
import devsec.app.easykitchen.api.RestApiService
import devsec.app.easykitchen.api.RetrofitInstance
import devsec.app.easykitchen.data.models.Category
import devsec.app.easykitchen.data.models.Food
import devsec.app.easykitchen.data.models.Recette
import devsec.app.easykitchen.ui.main.adapter.CategoryAdapter
import devsec.app.easykitchen.ui.main.adapter.RecommendedFoodAdapter
import devsec.app.easykitchen.ui.main.adapter.FoodAdapter
import devsec.app.easykitchen.ui.main.view.CategoryRecipesFilterActivity
import devsec.app.easykitchen.ui.main.view.FoodRecipeActivity
import devsec.app.easykitchen.ui.main.view.MainMenuActivity
import devsec.app.easykitchen.utils.services.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime

class HomeFragment : Fragment() {
    //************* Recommended food *************//
    private lateinit var recommendedFoodRecyclerView: RecyclerView
    private lateinit var recommendedFoodAdapter: RecommendedFoodAdapter
    //************* Category **********************//
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    //************** Expert Recipes **************//
    private lateinit var expertRecipesRecyclerView: RecyclerView
    private lateinit var expertRecipesAdapter: FoodAdapter
    private lateinit var title: TextView
    //***************** Recycler View lists *************//
    private lateinit var recetteList: ArrayList<Recette>
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var foodList: ArrayList<Food>

    //***************** Loading Dialog *************//
    private lateinit var loadingDialog: LoadingDialog





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navDrawerButton = view.findViewById<Button>(R.id.menu)
        val drawerLayout = (activity as MainMenuActivity).drawerLayout
        navDrawerButton.setOnClickListener {
            drawerLayout.open()
        }
        navDrawerButton.setOnClickListener {

        }
        //************* Recommended food *************//

        val layoutManager = LinearLayoutManager(context)
        title = view.findViewById(R.id.title)
        recommendedFoodRecyclerView = view.findViewById(R.id.recommendedView)
        recommendedFoodRecyclerView.setHasFixedSize(true)

//        initRecette()

        //*********** Category Recycler View Implementation ***********//

        initCategoryList()
        val categoryLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView = view.findViewById(R.id.categorieView)
        categoryRecyclerView.layoutManager = categoryLayoutManager
        categoryRecyclerView.setHasFixedSize(true)
        categoryAdapter = CategoryAdapter(categoryList)
        categoryRecyclerView.adapter = categoryAdapter
        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, CategoryRecipesFilterActivity::class.java)
                intent.putExtra("category", categoryList[position].name)
                startActivity(intent)
            }
        })

        //*********** Expert Recipes Recycler View Implementation ***********//
        initFoodList()
        val expertRecipesTextHeader = view.findViewById<TextView>(R.id.expertText)
        ("Expert Recipe for " +dailyFood()).also { expertRecipesTextHeader.text = it }
        val expertRecipesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        expertRecipesRecyclerView = view.findViewById(R.id.expertView)
        expertRecipesRecyclerView.layoutManager = expertRecipesLayoutManager
        expertRecipesRecyclerView.setHasFixedSize(true)
        expertRecipesAdapter = FoodAdapter(foodList)
        expertRecipesRecyclerView.adapter = expertRecipesAdapter
        expertRecipesAdapter.setOnItemClickListener(object : FoodAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, FoodRecipeActivity::class.java)
                intent.putExtra("id", foodList[position].id)
                startActivity(intent)
            }
        })

        val next = view.findViewById<Button>(R.id.menu)
//
//        next.setOnClickListener() {
//            val intent = Intent(context, RecetteActivity::class.java)
//            startActivity(intent)
//        }



    }

    //******************************** Init Recette List **********************************************//

    private fun initRecette() {

//        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
//
//        retIn.getRecette().enqueue(object : Callback<RecettesInQueue> {
//            override fun onResponse(
//                call: Call<RecettesInQueue>,
//                response: Response<RecettesInQueue>
//            ) {
//
//                recommendedFoodAdapter = RecommendedFoodAdapter(response.body()!!.recettes)
//                recommendedFoodRecyclerView.adapter = recommendedFoodAdapter
//                recommendedFoodRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
//
//
//            }
//
//            override fun onFailure(call: Call<RecettesInQueue>, t: Throwable) {
//                Log.d("400","Failure = "+t.toString());
//            }
//
//        })

    }


    //****************************************** init Category **********************************//

    private fun initCategoryList() {
        categoryList = ArrayList()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getCategoriesList()
        call.enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    val categories = response.body()
                    for (category in categories!!) {
                        categoryList.add(category)
                        }
                    }
                categoryAdapter.notifyDataSetChanged()
                }
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d("400","Failure = "+t.toString());
            }

        })
    }

    //****************************************** init food *********************************************//
    private fun initFoodList() {
        foodList = ArrayList()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getFoodsList()
        call.enqueue((object : Callback<List<Food>> {
            override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                if (response.isSuccessful) {
                    var foods = response.body()
                    for (food in foods!!) {
                        if (dailyFood()=="Breakfast"){
                            if (food.category=="Breakfast"){ foodList.add(food)}
                        }
                        if (dailyFood()=="Lunch"){
                            if (food.category=="Lamb" || food.category=="Pasta" || food.category=="Beef"){ foodList.add(food)}
                        }
                        if (dailyFood()=="Dinner"){
                            if (food.category=="Side" ||food.category=="Starter"){ foodList.add(food)}
                        }
                    }
                    val randomFood = foodList.random()
                    foodList.clear()
                    foodList.add(randomFood)
                    expertRecipesAdapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                Log.d("400","Failure = "+t.toString());
            }

        }))
    }

    private fun dailyFood (): String {
        val current = LocalTime.now()
        val morning = LocalTime.of(6,0)
        val afternoon = LocalTime.of(12,0)
        val evening = LocalTime.of(18,0)
        if (current.isAfter(morning) && current.isBefore(afternoon)){ return "Breakfast"}
        else if (current.isAfter(afternoon) && current.isBefore(evening)){ return "Lunch"}
        return "Dinner"

    }


}