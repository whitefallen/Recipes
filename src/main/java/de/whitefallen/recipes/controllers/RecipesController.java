package de.whitefallen.recipes.controllers;

import de.whitefallen.recipes.domain.Recipe;
import de.whitefallen.recipes.domain.User;
import de.whitefallen.recipes.repositories.RecipeRepository;
import de.whitefallen.recipes.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RecipesController {

    private final RecipeRepository recipeRepository;
    private final CustomUserDetailsService userService;

    @Autowired
    public RecipesController(RecipeRepository recipeRepository, CustomUserDetailsService userService) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
    }


    @GetMapping("/recipes")
    public ModelAndView getAllRecipes() {
        ModelAndView modelAndView = new ModelAndView();
        List<Recipe> recipes = recipeRepository.findAll();
        modelAndView.addObject("recipes", recipes);
        modelAndView.setViewName("recipes");
        return modelAndView;
    }

    @GetMapping("/recipes/add")
    public ModelAndView addRecipe() {
        ModelAndView modelAndView = new ModelAndView();
        Recipe recipe = new Recipe();
        modelAndView.addObject("recipe", recipe);
        modelAndView.setViewName("addRecipe");
        return modelAndView;
    }

    @PostMapping("/recipes/add")
    public ModelAndView createRecipe(Recipe recipe, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        if(recipe != null) {
            recipe.setUser(user);
            recipeRepository.save(recipe);
            modelAndView.addObject("successMessage", "Recipe has been added successfully");
            modelAndView.addObject("recipe", new Recipe());
            modelAndView.setViewName("redirect:/recipes");
        } else {
            modelAndView.setViewName("redirect:/addRecipe");
        }
        return modelAndView;
    }

    @GetMapping("/recipes/{id}")
    public ModelAndView getRecipeById(@PathVariable("id") String id) {
        ModelAndView modelAndView = new ModelAndView();
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        modelAndView.addObject("recipe", recipe);
        modelAndView.setViewName("recipe");
        return modelAndView;
    }
}
