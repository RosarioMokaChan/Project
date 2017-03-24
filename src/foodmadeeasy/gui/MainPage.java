/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.gui;

import foodmadeeasy.connection.ConnectionHandlerAdmin;
import foodmadeeasy.connection.ConnectionHandlerIngredient;
import foodmadeeasy.connection.ConnectionHandlerRecipeIngredient;
import foodmadeeasy.generic.AdminUser;
import foodmadeeasy.generic.EncryptDecrypt;
import foodmadeeasy.generic.FullRecipeDetails;
import foodmadeeasy.generic.Ingredient;
import foodmadeeasy.generic.Recipe;
import foodmadeeasy.generic.RecipeIngredient;
import foodmadeeasy.generic.SearchForMatchingRecipes;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author chris
 */
public class MainPage extends javax.swing.JFrame {
  
    private ArrayList<Ingredient> selectedIngredients = new ArrayList();
    private ArrayList<FullRecipeDetails> allRecipes = new ArrayList();
    private ArrayList<Ingredient> allIngredients = new ArrayList();
    private ArrayList<FullRecipeDetails> filteredRecipes = new ArrayList();
    
    private DefaultComboBoxModel cbModel;
    private DefaultComboBoxModel cbSortModel;
    private DefaultComboBoxModel cbSearchFilteredModel;
    private DefaultListModel liIngredientsModel = new DefaultListModel();
    private DefaultListModel liRecipesModel = new DefaultListModel();
    
    /**
     * Creates new form MainPage
     */
    public MainPage() {
        initComponents();
        //setup();
        setupDesign();
    }
    
    /**
     * Function to search for recipes with ingredients matching the entered ingredients
     */
    private void updateRecipeList(){
        //if there are ingredients in the list, find matching recipes
        if(!selectedIngredients.isEmpty()){
            //get the returned recipes
            filteredRecipes = new ArrayList();
            //if getting exact matches is selected, only get recipes matching exactly, otherwise return with normal limits
            if(cbExact.isSelected()){
                filteredRecipes = SearchForMatchingRecipes.searchViaIngredients(selectedIngredients, allRecipes, true);
            }else{
                filteredRecipes = SearchForMatchingRecipes.searchViaIngredients(selectedIngredients, allRecipes, false);
            }
            //update list model and display recipes
            liRecipesModel = new DefaultListModel();
            for(int i = 0; i < filteredRecipes.size(); i++){
                liRecipesModel.addElement(filteredRecipes.get(i).getRecipeName());// + " - Difficulty: " + filteredRecipes.get(i).getDifficulty() + "/10");
                //force sort
                cbSort.setSelectedIndex(cbSort.getSelectedIndex());
            }
            liReturnedRecipes.setModel(liRecipesModel);
            if(!filteredRecipes.isEmpty()){
                liReturnedRecipes.setSelectedIndex(0);
                lblRecipeName.setText("<html>" + filteredRecipes.get(0).getRecipeName() + "<br>Difficulty: " + filteredRecipes.get(0).getDifficulty() + "/10" + "<br>Time: " + filteredRecipes.get(0).getTime() + "mins</html>");
                lblRecipeDescription.setText(filteredRecipes.get(0).getDescription());
            }else{
                lblRecipeName.setText("");
                lblRecipeDescription.setText("");
            }
        }else{//otherwise clear the recipe list
            liRecipesModel = new DefaultListModel();
            liReturnedRecipes.setModel(liRecipesModel);
        }
    }
    
    /**
     * Function to take the entered name from the combo box and add to the
     * appropriate filter list
     */
    private void addToFilter(){
        //if the option to search with ingredient name is selected:
        if(rbIngredientName.isSelected()){
            lblSelectedIngredients.setText("Selected Ingredients:");
            //if the combo box actually contains something
            if(cbSearch.getSelectedItem() != null){
                //display to the user if the ingredient that they are trying to add has
                //already been added, and if so return without adding to list
                for(int i = 0; i < liIngredientsModel.getSize(); i++){
                    if(liIngredientsModel.get(i).toString().equalsIgnoreCase(cbSearch.getSelectedItem().toString())){
                        lblSelectedIngredients.setText("You have already added this ingredient to the filter");
                        return;
                    }
                }
                //update model for ingredients list
                liIngredientsModel.addElement(cbSearch.getSelectedItem());
                liIngredients.setModel(liIngredientsModel);
                if(!liIngredientsModel.isEmpty()) liIngredients.setSelectedIndex(0);
                //getting the names of the ingredients and then using them to search for
                //recipes
                selectedIngredients = new ArrayList();
                for(int i = 0; i < liIngredientsModel.getSize(); i++){
                    selectedIngredients.add(new Ingredient(liIngredientsModel.getElementAt(i).toString()));
                }
                //update shown recipes
                updateRecipeList();
            }
        }else if(rbRecipeName.isSelected()){
            //otherwise search using recipe name
            lblRecipes.setText("Recipes:");
            //if the combo box isn't empty
            if(cbSearch.getSelectedItem() != null){
                //again, if the recipe already exists in the list, display to user 
                //and don't add to the list again
                for(int i = 0; i < liRecipesModel.getSize(); i++){
                    if(liRecipesModel.get(i).toString().equalsIgnoreCase(cbSearch.getSelectedItem().toString())){
                        lblRecipes.setText("You have already added this recipe to the filter");
                        return;
                    }
                }
                //update model for recipes list
                liRecipesModel.addElement(cbSearch.getSelectedItem());
                liReturnedRecipes.setModel(liRecipesModel);
                //force sort
                cbSort.setSelectedIndex(cbSort.getSelectedIndex());
                //set the newest addition as active
                if(!liRecipesModel.isEmpty()) liReturnedRecipes.setSelectedIndex(0);
            }
        }
        cbSearch.setModel(cbModel);
        cbSearch.setSelectedItem(null);
    }
    
    private void setupDesign(){
        //change colour of background away from default grey
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        Container a = this.getContentPane();
        a.setBackground(Color.white);
        //disable button by default, enabled when a recipe is selected later on
        btnViewRecipe.setEnabled(false);
        cbExact.setSelected(false);
        
        //fill out sorting combo box
        String[] sortNames = new String[] {"Name: A-Z", "Name: Z-A", "Difficulty: Low-High", "Difficulty: High-Low", "Time: Low-High", "Time: High-Low"};
        //set the model for the combo box
        cbSortModel = new DefaultComboBoxModel(sortNames);
        cbSort.setModel(cbSortModel);
        
        //change properties of JList to allow when disable to still retain black text colour
        liIngredients.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                this.setEnabled(true);
                return this;
            }
        });
        
        //get all ingredients from the database
        ConnectionHandlerIngredient ingredientHandler = new ConnectionHandlerIngredient();
        allIngredients = ingredientHandler.getAllIngredients();
        //if list is empty, most likely a problem getting data from the database
        if(allIngredients.isEmpty()) JOptionPane.showMessageDialog(null, "There was a problem getting the ingredients from the database", "Error connecting to database", JOptionPane.ERROR_MESSAGE);
        
        //get a list of every recipe
        ConnectionHandlerRecipeIngredient recipes = new ConnectionHandlerRecipeIngredient();
        allRecipes = recipes.getAllRecipesIngredients();
        //again, if empty presume something is wrong
        if(allRecipes.isEmpty()) JOptionPane.showMessageDialog(null, "There was a problem getting the recipes from the database", "Error connecting to database", JOptionPane.ERROR_MESSAGE);
        
        //set default to searching for ingredients
        searchIngredients();
        //create listeners required
        createListeners();
    }
    
    private void searchRecipes(){
        //convert list of recipes to array of names to be used for cb model
        String[] recipeNames = new String[allRecipes.size()];
        for(int i = 0; i < allRecipes.size(); i++){
            recipeNames[i] = allRecipes.get(i).getRecipeName();
        }
        //set the model for the combo box
        cbModel = new DefaultComboBoxModel(recipeNames);
        //empty the models for the lists
        liRecipesModel = new DefaultListModel();
        liReturnedRecipes.setModel(liRecipesModel);
        liIngredientsModel = new DefaultListModel();
        liIngredients.setModel(liIngredientsModel);
        
        cbSearch.setModel(cbModel);
        cbSearch.setSelectedItem(null);
    }
    
    private void searchIngredients(){
        //convert list of ingredients to array of names to be used for cb model
        String[] ingredientNames = new String[allIngredients.size()];
        for(int i = 0; i < allIngredients.size(); i++){
            ingredientNames[i] = allIngredients.get(i).getName();
        }
        //set the model for the combo box
        cbModel = new DefaultComboBoxModel(ingredientNames);
        //empty the models for the lists
        liIngredientsModel = new DefaultListModel();
        liIngredients.setModel(liIngredientsModel);
        liRecipesModel = new DefaultListModel();
        liReturnedRecipes.setModel(liRecipesModel);
        
        cbSearch.setModel(cbModel);
        cbSearch.setSelectedItem(null);
    }
    
    /**
     * Function to return arraylist of all entries which match the users' entered text
     * @param enteredText - string, users' text
     * @return arraylist of all items starting with the text
     */
    private ArrayList<Object> lookUp(String enteredText){
        ArrayList<Object> list = new ArrayList();
        for(int i = 0; i < cbModel.getSize(); i++){
            Object currentItem = cbModel.getElementAt(i);
            if(currentItem.toString().toLowerCase().startsWith(enteredText.toLowerCase())){
                list.add(currentItem);
            }
        }
        //if nothing matches, return nothing, otherwise return list
        if(list.isEmpty()){
            return null;
        }else{
            return list;
        }
    }
    
    /**
     * Updates the search combo box based on user input
     */
    private void updateSearchBox(){
        //empty previous filter model and get user text
        cbSearchFilteredModel = new DefaultComboBoxModel();
        String userText = cbSearch.getEditor().getItem().toString();
        //get objects starting with user text
        ArrayList<Object> items = lookUp(userText);
        //select the items
        if(items != null){
            for(int i = 0; i < items.size(); i++){
                cbSearchFilteredModel.addElement(items.get(i));
            }
        }
        cbSearchFilteredModel.setSelectedItem(userText);
        cbSearch.setModel(cbSearchFilteredModel);

        if (cbSearch.isDisplayable() && items != null){
            cbSearch.setPopupVisible(true);
        }else{
            cbSearch.setPopupVisible(false);
        }
    }
    
    /**
     * Function for creating listeners for containers
     * e.g. checking when enter key is hit in combo box
     */
    private void createListeners(){
        //add key listener to combo box so hitting enter while inside combo box will allow
        //an action to be performed
        cbSearch.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_ENTER://if enter, add users' choice to selected ingredients
                        if (cbSearch.getEditor().getItem().toString().isEmpty()){
                            System.out.println("please dont make me blank");
                            //its empty
                        }else{
                            System.out.println("yay, i have something inside me");
                            //get the ingredient from the list
                            addToFilter();
                        }//end if contents is empty 
                        break;
                    case KeyEvent.VK_BACK_SPACE://if backspace and this makes text field empty, disable dropdown
                        if (cbSearch.getEditor().getItem().toString().isEmpty()){
                            if (cbSearch.isDisplayable()){
                                cbSearch.setPopupVisible(false);
                                cbSearch.setModel(cbModel);
                                cbSearch.setSelectedItem(null);
                            }
                        }else{
                            updateSearchBox();
                        }
                        break;
                    case KeyEvent.VK_DOWN://if arrow keys are used, don't update search bar
                        break;
                    case KeyEvent.VK_UP:
                        break;
                    default://if the key press is anything else:
                        updateSearchBox();
                        break;
                }
            }//end void
        });//end
        
        //listener for checking when a new item is selected from the ingredients jList
        liIngredients.addListSelectionListener((ListSelectionEvent arg0) -> {
            if (!arg0.getValueIsAdjusting()) {
                System.out.println(liIngredients.getSelectedValue());
            }
        });
        
        //listener for right clicking on an ingredient
        liIngredients.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //if right click and list is enabled and is not empty
                if (SwingUtilities.isRightMouseButton(e) && liIngredients.isEnabled() && !liIngredientsModel.isEmpty()) {   
                   liIngredients.setSelectedIndex(liIngredients.locationToIndex(e.getPoint()));
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem item = new JMenuItem("Remove");
                    item.addActionListener((ActionEvent e1) -> {
                        removeFromList();
                   });
                    menu.add(item);
                    //make menu appear right next to cursor
                    menu.show(liIngredients, e.getPoint().x, e.getPoint().y);            
                }
            }
        });
        
        //listener for right clicking on a recipe
        liReturnedRecipes.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //if right click and is not empty, also if other list is not active
                if (SwingUtilities.isRightMouseButton(e) && !liIngredients.isEnabled() && !liRecipesModel.isEmpty()) {   
                   liReturnedRecipes.setSelectedIndex(liReturnedRecipes.locationToIndex(e.getPoint()));
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem item = new JMenuItem("Remove");
                    item.addActionListener((ActionEvent e1) -> {
                        removeFromList();
                   });
                    menu.add(item);
                    //make menu appear right next to cursor
                    menu.show(liReturnedRecipes, e.getPoint().x, e.getPoint().y);            
                }
            }
        });
        
        //listener for the recipes jlist
        liReturnedRecipes.addListSelectionListener((ListSelectionEvent arg0) -> {
            if (!arg0.getValueIsAdjusting()) {
                //if nothing is selected, disable the button,
                //otherwise enable it
                if(liReturnedRecipes.isSelectionEmpty()) {
                    btnViewRecipe.setEnabled(false);
                }else{
                    btnViewRecipe.setEnabled(true);
                }
                //do something based on radio button
                if(rbRecipeName.isSelected()){
                    //loop through all recipes and find if the added recipe exists
                    liIngredientsModel.clear();
                    boolean exists = false;
                    for(int i = 0; i < allRecipes.size(); i++){
                        //if the added recipe exists in the list
                        if(allRecipes.get(i).getRecipeName().equalsIgnoreCase(liReturnedRecipes.getSelectedValue())){
                            exists = true;
                            //get details of recipe and fill with all ingedients in recipe
                            FullRecipeDetails selectedRecipe = allRecipes.get(i);
                            //update labels to show details
                            lblRecipeName.setText("<html>" + selectedRecipe.getRecipeName() + "<br>Difficulty: " + selectedRecipe.getDifficulty() + "/10" + "<br>Time: " + selectedRecipe.getTime() + "mins</html>");
                            lblRecipeDescription.setText(selectedRecipe.getDescription());
                            
                            for(int j = 0; j < selectedRecipe.getIngredients().size(); j++){
                                //update model for ingredients list
                                liIngredientsModel.addElement(selectedRecipe.getIngredients().get(j).getName() + " (" + selectedRecipe.getRecipeIngredients().get(i).getQuantities().get(j) + ")");
                            }
                            liIngredients.setModel(liIngredientsModel);
                        }
                    }
                    //if no recipe with that name is found, say so
                    if(!exists){
                        lblRecipeName.setText("");
                        lblRecipeDescription.setText("");
                    }
                }else if(rbIngredientName.isSelected()){
                    //search through all recipe in the returned list
                    for(int i = 0; i < filteredRecipes.size(); i++){
                        //if the searched for recipe exists, get and display details
                        if(filteredRecipes.get(i).getRecipeName().equalsIgnoreCase(liReturnedRecipes.getSelectedValue())){
                            FullRecipeDetails selectedRecipe = filteredRecipes.get(i);
                            //update labels to show details
                            lblRecipeName.setText("<html>" + selectedRecipe.getRecipeName() + "<br>Difficulty: " + selectedRecipe.getDifficulty() + "/10" + "<br>Time: " + selectedRecipe.getTime() + "mins</html>");
                            lblRecipeDescription.setText(selectedRecipe.getDescription());
                        }
                    }
                }  
            }
        });
        
        cbExact.addActionListener((ActionEvent e) ->{
            System.out.println(cbExact.isSelected());
            updateRecipeList();
        });
        
        //listener for searching via ingredients
        //reset some things ready for the next lot of stuff
        rbIngredientName.addActionListener((ActionEvent e) -> {
            liIngredients.setEnabled(true);
            selectedIngredients = new ArrayList();
            cbExact.setEnabled(true);
            cbExact.setSelected(false);
            lblRecipeName.setText("");
            lblRecipeDescription.setText("");
            lblSelectedIngredients.setText("Selected Ingredients:");
            searchIngredients();
        });

        //listener for searching via recipe
        //reset things again
        rbRecipeName.addActionListener((ActionEvent e) -> {
            liIngredients.setEnabled(false);
            selectedIngredients = new ArrayList();
            cbExact.setSelected(false);
            cbExact.setEnabled(false);
            lblRecipeName.setText("");
            lblRecipeDescription.setText("");
            lblSelectedIngredients.setText("Ingredients for recipe:");
            searchRecipes();
        });
        
        cbSort.addActionListener ((ActionEvent e) -> {
            ArrayList<String> sort = new ArrayList();
            //add contents of model to arraylist
            for(int i = 0; i < liRecipesModel.getSize(); i++){
                sort.add(liRecipesModel.getElementAt(i).toString());
            }
            //sort the list according to the action selected
            switch(cbSort.getSelectedIndex()){
                case 0: 
                        sort = sortBy(sort, 0, 2);
                        break;
                case 1: 
                        sort = sortBy(sort, 1, 2);
                        break;
                case 2: 
                        sort = sortBy(sort, 0, 0);
                        break;
                case 3: 
                        sort = sortBy(sort, 1, 0);
                        break;
                case 4: 
                        sort = sortBy(sort, 0, 1);
                        break;
                case 5: 
                        sort = sortBy(sort, 1, 1);
                        break;
            }
            //add contents back into model
            liRecipesModel = new DefaultListModel();
            for(int i = 0; i < sort.size(); i++){
                liRecipesModel.addElement(sort.get(i));
            }
            liReturnedRecipes.setModel(liRecipesModel);
            liReturnedRecipes.setSelectedIndex(0);
        });   
    }
    
    /**
     * Function to sort a recipes by given criteria
     * @param contents - Arraylist of recipe names
     * @param sortOrder - 0=ascending, 1=descending
     * @param type - 0 = difficulty, 1 = time required, anything else = alphabetically
     * @return ArrayList of sorted recipes names according to condition
     */
    private ArrayList<String> sortBy(ArrayList<String> contents, int sortOrder, int type){
        //used to store recipes with details based on type, difficulty is just a generic term
        //this method eradicates any recipes added by the user which don't exist
        ArrayList<String> sortedDifficulties = new ArrayList();
        Map<String, Integer> recipeWithDifficulty = new HashMap();
        //if type is 0, then sort by difficulty
        switch (type) {
            case 0:
                //loop through all recipes and contents to see if they match
                for(int i = 0; i < allRecipes.size(); i++){
                    for(int j = 0; j < contents.size(); j++){
                        //if the current recipe is in the list and exists in the global scale,
                        //create an instance of it
                        if(allRecipes.get(i).getRecipeName().equalsIgnoreCase(contents.get(j))){
                            FullRecipeDetails recipe = allRecipes.get(i);
                            recipeWithDifficulty.put(recipe.getRecipeName(), recipe.getDifficulty());
                            break;
                        }
                    }
                }   break;
            case 1:
                //otherwise sort by time required if type is 1
                //loop through all recipes and contents to see if they match
                for(int i = 0; i < allRecipes.size(); i++){
                    for(int j = 0; j < contents.size(); j++){
                        //if the current recipe is in the list and exists in the global scale,
                        //create an instance of it
                        if(allRecipes.get(i).getRecipeName().equalsIgnoreCase(contents.get(j))){
                            FullRecipeDetails recipe = allRecipes.get(i);
                            recipeWithDifficulty.put(recipe.getRecipeName(), recipe.getTime());
                            break;
                        }
                    }
                }   break;
            default:
                //search alphabetically if type is anything else
                //loop through all recipes and contents to see if they match
                for(int i = 0; i < allRecipes.size(); i++){
                    for(int j = 0; j < contents.size(); j++){
                        //if the current recipe is in the list and exists in the global scale,
                        //create an instance of it
                        if(allRecipes.get(i).getRecipeName().equalsIgnoreCase(contents.get(j))){
                            FullRecipeDetails recipe = allRecipes.get(i);
                            //add to final arraylist instead of map, then sort bypassing map sorting
                            sortedDifficulties.add(recipe.getRecipeName());
                            break;
                        }
                    }
                }
                //search order depends on parameter then return sorted list
                if(sortOrder == 0){
                    Collections.sort(sortedDifficulties);
                }else{
                    Collections.sort(sortedDifficulties, Collections.reverseOrder());
                }
                return sortedDifficulties;
        }
        //http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
        //sorting in order by converting to stream and sorting then adding each value back to array
        if(sortOrder == 0){
            recipeWithDifficulty.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach((entry) -> {
            sortedDifficulties.add(entry.getKey());
            });
        }else{
            recipeWithDifficulty.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach((entry) -> {
            sortedDifficulties.add(entry.getKey());
            });
        }
        return sortedDifficulties;
    }
    
    /**
     * Function to remove a selected item from a currently active list
     */
    private void removeFromList(){
        if(rbIngredientName.isSelected()){
            //if the model is empty then do nothing
            if(!liIngredientsModel.isEmpty()) {
                //if nothing has been selected but button is hit, remove top ingredient
                if(liIngredients.isSelectionEmpty()){
                    liIngredientsModel.remove(0);
                }else{
                    //otherwise remove selected ingredient
                    liIngredientsModel.remove(liIngredients.getSelectedIndex());
                }
                liIngredients.setModel(liIngredientsModel);
                //reset selected ingredients list
                selectedIngredients = new ArrayList();
                for(int i = 0; i < liIngredientsModel.getSize(); i++){
                    selectedIngredients.add(new Ingredient(liIngredientsModel.getElementAt(i).toString()));
                }
                //update shown recipes
                updateRecipeList();
                if(!liIngredientsModel.isEmpty()) liIngredients.setSelectedIndex(0);
            }
        }else if(rbRecipeName.isSelected()){
            //if the model is empty then do nothing
            if(!liRecipesModel.isEmpty()) {
                //if nothing has been selected but button is hit, remove top ingredient
                if(liReturnedRecipes.isSelectionEmpty()){
                    liRecipesModel.remove(0);
                }else{
                    //otherwise remove selected ingredient
                    liRecipesModel.remove(liReturnedRecipes.getSelectedIndex());
                }
                liReturnedRecipes.setModel(liRecipesModel);
                if(!liRecipesModel.isEmpty()) liReturnedRecipes.setSelectedIndex(0);
            }
        }
    }
    
    /*
    private void setup(){
        this.setLocationRelativeTo(null);
        ConnectionHandlerAdmin admins = new ConnectionHandlerAdmin();
        //admins.createAdmin("Admin", "Admin");
        AdminUser admin = admins.getAdmin("Admin").get(0);
        String decryptedPassword = EncryptDecrypt.decryptPassword(admin.getPassword(), admin.getKey());
        System.out.println(decryptedPassword);
        
        ConnectionHandlerRecipeIngredient recipes = new ConnectionHandlerRecipeIngredient();
        ArrayList<FullRecipeDetails> recipeIngredients = recipes.getAllRecipesIngredients();
        
        //print out all returned recipes with ingredients
        for(int i = 0; i < recipeIngredients.size(); i++){
            System.out.println("Recipe " + recipeIngredients.get(i).getRecipeId() + " : " + recipeIngredients.get(i).getRecipeName());
            for(int j = 0; j < recipeIngredients.get(i).getIngredients().size(); j ++){
                System.out.println("    Ingredient " + recipeIngredients.get(i).getIngredients().get(j).getId() + " : " + recipeIngredients.get(i).getIngredients().get(j).getName() + " Quantity: " + recipeIngredients.get(i).getRecipeIngredients().get(i).getQuantities().get(j));
            }
        }
        
        Ingredient ing1 = new Ingredient("TEST INGREDIENT 1");
        Ingredient ing2 = new Ingredient("TEST INGREDIENT 2");
        Ingredient ing3 = new Ingredient("TEST INGREDIENT 4");
        ArrayList<Ingredient> listing = new ArrayList();
        listing.add(ing1);
        listing.add(ing2);
        listing.add(ing3);
        ArrayList<FullRecipeDetails> filter = SearchForMatchingRecipes.searchViaIngredients(listing, recipeIngredients);
        //print out all returned recipes with ingredients
        for(int i = 0; i < filter.size(); i++){
            System.out.println("Searched Recipe " + i + " : " + filter.get(i).getRecipeName());
        }
        
        Recipe newRecipe = new Recipe("Beef Noodles", "noodles.png", 2, "Beef with crispy parsnips, pretty bad on their own", "Just make beef and parsnips, like really...", "Main Meal", 50);
        
        RecipeIngredient ri = new RecipeIngredient(newRecipe.getName(), ing1.getName(), "100 Tonnes");
        ri.addIngredient(ing2.getName(), "1 PentaTonne");
        ri.addIngredient(ing3.getName(), "1 OmegaGram");
        ArrayList<RecipeIngredient> riList = new ArrayList();
        riList.add(ri);
        FullRecipeDetails newRecipeFull = new FullRecipeDetails(newRecipe.getName(), newRecipe.getImageurl(), newRecipe.getDifficulty(), newRecipe.getDesc(), newRecipe.getTime(), newRecipe.getMethod(), newRecipe.getType(), listing, riList);
        
        //WHEN EDITING A RECIPE MAKE SURE TO GET CORRECT ID AS UPDATED RECIPE GETS PUT TO BOTTOM OF LIST
        recipeIngredients.get(1).setRecipeName("ULTIMATE TEST V2");
        recipeIngredients.get(1).setIngredients(listing);
        recipeIngredients.get(1).setRecipeIngredients(riList);
        //boolean didit = recipes.editRecipe(recipeIngredients.get(1));
        //boolean didit = recipes.addRecipe(newRecipeFull);
        //System.out.println("editing recipe: "+didit);
        ConnectionHandlerIngredient ingredientHandler = new ConnectionHandlerIngredient();
        ArrayList<Ingredient> ingreds = ingredientHandler.getAllIngredients();
        for(int i = 0; i<ingreds.size(); i++) System.out.println("Ingredient " + ingreds.get(i).getId() + " " + ingreds.get(i).getName());
        //System.out.println(recipes.deleteRecipe(recipeIngredients.get(2).getRecipeId()));
    }
    */
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgNameSelection = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        btnAdminLogin = new javax.swing.JButton();
        pIngredients = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        liIngredients = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        liReturnedRecipes = new javax.swing.JList<>();
        lblRecipeName = new javax.swing.JLabel();
        lblRecipeDescription = new javax.swing.JLabel();
        btnViewRecipe = new javax.swing.JButton();
        lblRecipes = new javax.swing.JLabel();
        lblSelectedIngredients = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnAddToFilter = new javax.swing.JButton();
        cbSearch = new javax.swing.JComboBox<>();
        rbIngredientName = new javax.swing.JRadioButton();
        rbRecipeName = new javax.swing.JRadioButton();
        btnRemoveIngredient = new javax.swing.JButton();
        cbSort = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        cbExact = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Food Made Easy");

        jLabel1.setFont(new java.awt.Font("Segoe Print", 0, 24)); // NOI18N
        jLabel1.setText("Food Made Easy");

        btnAdminLogin.setText("Log-in Admin");
        btnAdminLogin.setNextFocusableComponent(cbSearch);
        btnAdminLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminLoginActionPerformed(evt);
            }
        });

        liIngredients.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(liIngredients);

        jLabel2.setText("Name:");

        jScrollPane3.setViewportView(liReturnedRecipes);

        lblRecipeName.setText(" ");

        lblRecipeDescription.setText(" ");

        btnViewRecipe.setText("View Details");
        btnViewRecipe.setNextFocusableComponent(btnAdminLogin);
        btnViewRecipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewRecipeActionPerformed(evt);
            }
        });

        lblRecipes.setText("Recipes:");

        lblSelectedIngredients.setText("Selected Ingredients:");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnAddToFilter.setText("Add to Filter");
        btnAddToFilter.setFocusCycleRoot(true);
        btnAddToFilter.setNextFocusableComponent(btnRemoveIngredient);
        btnAddToFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToFilterActionPerformed(evt);
            }
        });

        cbSearch.setEditable(true);
        cbSearch.setNextFocusableComponent(btnAddToFilter);

        bgNameSelection.add(rbIngredientName);
        rbIngredientName.setSelected(true);
        rbIngredientName.setText("Search by ingredients");
        rbIngredientName.setNextFocusableComponent(rbRecipeName);

        bgNameSelection.add(rbRecipeName);
        rbRecipeName.setText("Search by recipe name");
        rbRecipeName.setNextFocusableComponent(cbSort);

        btnRemoveIngredient.setText("Remove From Filter");
        btnRemoveIngredient.setNextFocusableComponent(rbIngredientName);
        btnRemoveIngredient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveIngredientActionPerformed(evt);
            }
        });

        cbSort.setNextFocusableComponent(btnViewRecipe);

        cbExact.setText("Show exact matches only");

        javax.swing.GroupLayout pIngredientsLayout = new javax.swing.GroupLayout(pIngredients);
        pIngredients.setLayout(pIngredientsLayout);
        pIngredientsLayout.setHorizontalGroup(
            pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIngredientsLayout.createSequentialGroup()
                .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pIngredientsLayout.createSequentialGroup()
                        .addGap(403, 403, 403)
                        .addComponent(lblRecipeName))
                    .addGroup(pIngredientsLayout.createSequentialGroup()
                        .addGap(403, 403, 403)
                        .addComponent(lblRecipeDescription))
                    .addGroup(pIngredientsLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnViewRecipe, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pIngredientsLayout.createSequentialGroup()
                                .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pIngredientsLayout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(44, 44, 44)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblSelectedIngredients))
                                .addGap(17, 17, 17)
                                .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pIngredientsLayout.createSequentialGroup()
                                        .addComponent(lblRecipes)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbSort, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(pIngredientsLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addGap(5, 5, 5)
                        .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbExact)
                            .addComponent(cbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRemoveIngredient, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddToFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbRecipeName)
                            .addComponent(rbIngredientName))))
                .addContainerGap(25, Short.MAX_VALUE))
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        pIngredientsLayout.setVerticalGroup(
            pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIngredientsLayout.createSequentialGroup()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pIngredientsLayout.createSequentialGroup()
                        .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddToFilter)
                            .addComponent(rbIngredientName))
                        .addGap(7, 7, 7)
                        .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRemoveIngredient)
                            .addComponent(rbRecipeName)
                            .addComponent(cbExact)))
                    .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pIngredientsLayout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addComponent(jLabel2))
                        .addGroup(pIngredientsLayout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(cbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15)
                .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSelectedIngredients)
                    .addComponent(lblRecipes)
                    .addComponent(cbSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(lblRecipeName)
                .addGap(7, 7, 7)
                .addComponent(lblRecipeDescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(btnViewRecipe)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(248, 248, 248)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdminLogin)
                .addContainerGap())
            .addComponent(pIngredients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnAdminLogin))
                .addGap(18, 18, 18)
                .addComponent(pIngredients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdminLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminLoginActionPerformed
        JFrame adminPage = new AdminPageLogin(this);
        adminPage.setLocationRelativeTo(null);
        adminPage.setResizable(false);
        adminPage.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        adminPage.setVisible(true);
    }//GEN-LAST:event_btnAdminLoginActionPerformed

    private void btnAddToFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToFilterActionPerformed
        if(!cbSearch.getEditor().getItem().toString().isEmpty()) addToFilter();
    }//GEN-LAST:event_btnAddToFilterActionPerformed

    private void btnViewRecipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewRecipeActionPerformed
        //if model isn't empty and list has a selected value
        if(!liRecipesModel.isEmpty() && !liReturnedRecipes.isSelectionEmpty()) {
            //providing that something is selected, get the full details of the selected recipe
            FullRecipeDetails selectedRecipe = null;
            for(int i = 0; i < allRecipes.size(); i++){
                if(liReturnedRecipes.getSelectedValue().equalsIgnoreCase(allRecipes.get(i).getRecipeName())){
                    selectedRecipe = allRecipes.get(i);
                    break;
                }
            }
            //if the recipe exists launch and show details
            if(selectedRecipe != null){
                JFrame viewDetails = new ViewDetails(allRecipes, selectedRecipe);
                viewDetails.setLocationRelativeTo(null);
                viewDetails.setResizable(false);
                viewDetails.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                viewDetails.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnViewRecipeActionPerformed

    private void btnRemoveIngredientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveIngredientActionPerformed
        removeFromList();
    }//GEN-LAST:event_btnRemoveIngredientActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgNameSelection;
    private javax.swing.JButton btnAddToFilter;
    private javax.swing.JButton btnAdminLogin;
    private javax.swing.JButton btnRemoveIngredient;
    private javax.swing.JButton btnViewRecipe;
    private javax.swing.JCheckBox cbExact;
    private javax.swing.JComboBox<String> cbSearch;
    private javax.swing.JComboBox<String> cbSort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblRecipeDescription;
    private javax.swing.JLabel lblRecipeName;
    private javax.swing.JLabel lblRecipes;
    private javax.swing.JLabel lblSelectedIngredients;
    private javax.swing.JList<String> liIngredients;
    private javax.swing.JList<String> liReturnedRecipes;
    private javax.swing.JPanel pIngredients;
    private javax.swing.JRadioButton rbIngredientName;
    private javax.swing.JRadioButton rbRecipeName;
    // End of variables declaration//GEN-END:variables
}
