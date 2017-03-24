/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.gui;

import foodmadeeasy.connection.ConnectionHandlerIngredient;
import foodmadeeasy.connection.ConnectionHandlerRecipeIngredient;
import foodmadeeasy.generic.FullRecipeDetails;
import foodmadeeasy.generic.Ingredient;
import foodmadeeasy.generic.Recipe;
import foodmadeeasy.generic.RecipeIngredient;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author chris
 */
public class AddRecipe extends javax.swing.JFrame {

    //if true then a recipe is being edited,
    //if false then a new recipe is being added
    private boolean edit;
    private FullRecipeDetails recipe;
    private int editID;
    
    private ArrayList<Ingredient> allIngredients = new ArrayList();
    private TreeMap<String, String> ingredientDetails = new TreeMap();
    
    private DefaultComboBoxModel ingredientNamesModel;
    private DefaultComboBoxModel cbIngredientNamesFilteredModel;
    private DefaultListModel ingredientsModel;
    
    private ConnectionHandlerRecipeIngredient recipeConn = new ConnectionHandlerRecipeIngredient();
    
    /**
     * Creates new form AddRecipe
     */
    public AddRecipe(){
        initComponents();
    }
    
    /**
     * Creates new form AddRecipe
     * @param isEdit - used to decide whether to edit a recipe or make a new one
     * @param recipe - when editing a recipe, edit this recipe
     */
    public AddRecipe(boolean isEdit, FullRecipeDetails recipe) {
        initComponents();
        edit = isEdit;
        this.recipe = recipe;
        setup();
    }
    
    private void setup(){
        Container a = this.getContentPane();
        a.setBackground(Color.white);
        
        ingredientNamesModel = new DefaultComboBoxModel();
        ingredientsModel = new DefaultListModel();
        liIngredients.setModel(ingredientsModel);
        allIngredients = new ArrayList();
        ingredientDetails = new TreeMap();
        
        //reset the contents of everything
        txtRecipeName.setText("");
        txtImageURL.setText("");
        cbDifficulty.setSelectedIndex(0);
        txtTime.setText("");
        txtCategory.setText("");
        taDescription.setText("");
        taMethod.setText("");
        
        
        //get all ingredients from the database
        ConnectionHandlerIngredient ingredientHandler = new ConnectionHandlerIngredient();
        allIngredients = ingredientHandler.getAllIngredients();
        //if list is empty, most likely a problem getting data from the database
        if(allIngredients.isEmpty()) JOptionPane.showMessageDialog(null, "There was a problem getting the ingredients from the database", "Error connecting to database", JOptionPane.ERROR_MESSAGE);
        
        //populate dropdown with names of ingredients
        for(int i = 0; i < allIngredients.size(); i++){
            ingredientNamesModel.addElement(allIngredients.get(i).getName());
        }
        cbIngredientNames.setModel(ingredientNamesModel);
        cbIngredientNames.setSelectedItem(null);
        
        txtQuantity.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent event){
                if(event.getKeyCode() == KeyEvent.VK_ENTER){
                    addToList();
                }
            }
        });
        
        cbIngredientNames.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_ENTER://if enter, add users' choice to selected ingredients
                            addToList();
                        break;
                    case KeyEvent.VK_BACK_SPACE://if backspace and this makes text field empty, disable dropdown
                        if (cbIngredientNames.getEditor().getItem().toString().isEmpty()){
                            if (cbIngredientNames.isDisplayable()){
                                cbIngredientNames.setPopupVisible(false);
                                cbIngredientNames.setModel(ingredientNamesModel);
                                cbIngredientNames.setSelectedItem(null);
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
        if(edit) initEdit();//if editing, grab the details
    }
    
    private void addToList(){
        if(cbIngredientNames.getSelectedItem() != null && !txtQuantity.getText().isEmpty() && !cbIngredientNames.getEditor().getItem().toString().isEmpty()){
            //display to the user if the ingredient that they are trying to add has
            //already been added, and if so return without adding to list
            for(int i = 0; i < ingredientsModel.getSize(); i++){
                String full = ingredientsModel.get(i).toString();
                String[] parts = full.split("      -      Quantity: ");
                String name = parts[0];
                if(name.equalsIgnoreCase(cbIngredientNames.getSelectedItem().toString())){
                    lblIngredients.setText("You have already added this ingredient to the filter");
                    return;
                }
            }
            //update model for ingredients list
            ingredientsModel.addElement(cbIngredientNames.getSelectedItem().toString() + "      -      Quantity: " + txtQuantity.getText());
            liIngredients.setModel(ingredientsModel);
            //set the first item as the selected
            if(!ingredientsModel.isEmpty()) liIngredients.setSelectedIndex(0);
            //add the selected ingredient and quantity to the map
            ingredientDetails.put(cbIngredientNames.getSelectedItem().toString(), txtQuantity.getText());
            
            //update model with new item and clear selections
            cbIngredientNames.setModel(ingredientNamesModel);
            cbIngredientNames.setSelectedItem(null);
            txtQuantity.setText("");
            lblIngredients.setText(" ");
        }else{
            lblIngredients.setText("Please enter an ingredient name and quantity");
        }
    }
    
    /**
     * Updates the combo box based on user input
     */
    private void updateSearchBox(){
        //empty previous filter model and get user text
        cbIngredientNamesFilteredModel = new DefaultComboBoxModel();
        String userText = cbIngredientNames.getEditor().getItem().toString();
        //get objects starting with user text
        ArrayList<Object> items = lookUp(userText);
        //select the items
        if(items != null){
            for(int i = 0; i < items.size(); i++){
                cbIngredientNamesFilteredModel.addElement(items.get(i));
            }
        }
        cbIngredientNamesFilteredModel.setSelectedItem(userText);
        cbIngredientNames.setModel(cbIngredientNamesFilteredModel);

        if (cbIngredientNames.isDisplayable() && items != null){
            cbIngredientNames.setPopupVisible(true);
        }else{
            cbIngredientNames.setPopupVisible(false);
        }
    }
    
    /**
     * Function to return arraylist of all entries which match the users' entered text
     * @param enteredText - string, users' text
     * @return arraylist of all items starting with the text
     */
    private ArrayList<Object> lookUp(String enteredText){
        ArrayList<Object> list = new ArrayList();
        for(int i = 0; i < ingredientNamesModel.getSize(); i++){
            Object currentItem = ingredientNamesModel.getElementAt(i);
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
    
    //setup the form for editing an existing recipe
    private void initEdit(){
        System.out.println("Edit Recipe");
        ArrayList<FullRecipeDetails> allRecipes = recipeConn.getAllRecipesIngredients();
        //loop through all recipes
        for(int i = 0; i < allRecipes.size(); i++){
            //if the current recipe is the one passed through
            if(allRecipes.get(i).getRecipeId() == recipe.getRecipeId()){
                //if it is actually a recipe
                if(recipe!=null){
                    //grab the details
                    editID = recipe.getRecipeId();//used when editing
                    txtRecipeName.setText(recipe.getRecipeName());
                    txtImageURL.setText(recipe.getImageurl());
                    txtCategory.setText(recipe.getType());
                    txtTime.setText(String.valueOf(recipe.getTime()));
                    cbDifficulty.setSelectedIndex(recipe.getDifficulty()-1);
                    taDescription.setText(recipe.getDescription());
                    taMethod.setText(recipe.getMethod());
                    
                    //for every ingredient, add to map
                    for(int j = 0; j < recipe.getIngredients().size(); j++){
                        ingredientDetails.put(recipe.getIngredients().get(j).getName(), recipe.getRecipeIngredients().get(i).getQuantities().get(j));
                    }
                    //then from the map, add to the model
                    ingredientDetails.entrySet().forEach((entry) -> {
                        System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
                        ingredientsModel.addElement(entry.getKey() + "      -      Quantity: " + entry.getValue());
                    });
                    liIngredients.setModel(ingredientsModel);
                }
            }
        }
    }
    
    private boolean addTheRecipe(){
        //create recipe object (doesn't include ingredients)
        Recipe newRecipe = new Recipe(txtRecipeName.getText(), txtImageURL.getText(), Integer.parseInt(cbDifficulty.getSelectedItem().toString()), taDescription.getText(), taMethod.getText(), txtCategory.getText(), Integer.parseInt(txtTime.getText()));
        //add all ingredients to list
        //add mappings
        ArrayList<Ingredient> listing = new ArrayList();
        //create recipe ingredient object (links the recipe with the ingredients)
        RecipeIngredient ri = new RecipeIngredient(newRecipe.getName());
        ingredientDetails.entrySet().forEach((entry) -> {
            System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
            listing.add(new Ingredient(entry.getKey()));
            ri.addIngredient(entry.getKey(), entry.getValue());
        });
        
        ArrayList<RecipeIngredient> riList = new ArrayList();
        riList.add(ri);
        //finalise recipe object creation;
        FullRecipeDetails newRecipeFull = new FullRecipeDetails(newRecipe.getName(), newRecipe.getImageurl(), newRecipe.getDifficulty(), newRecipe.getDesc(), newRecipe.getTime(), newRecipe.getMethod(), newRecipe.getType(), listing, riList);
        boolean added = recipeConn.addRecipe(newRecipeFull);
        return added;
    }
    
    private boolean editTheRecipe(){
        //create recipe object (doesn't include ingredients)
        Recipe newRecipe = new Recipe(editID, txtRecipeName.getText(), txtImageURL.getText(), Integer.parseInt(cbDifficulty.getSelectedItem().toString()), taDescription.getText(), taMethod.getText(), txtCategory.getText(), Integer.parseInt(txtTime.getText()));
        //add all ingredients to list
        //add mappings
        ArrayList<Ingredient> listing = new ArrayList();
        //create recipe ingredient object (links the recipe with the ingredients)
        RecipeIngredient ri = new RecipeIngredient(newRecipe.getName());
        ingredientDetails.entrySet().forEach((entry) -> {
            System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
            listing.add(new Ingredient(entry.getKey()));
            ri.addIngredient(entry.getKey(), entry.getValue());
        });
        
        ArrayList<RecipeIngredient> riList = new ArrayList();
        riList.add(ri);
        //finalise recipe object creation;
        FullRecipeDetails newRecipeFull = new FullRecipeDetails(newRecipe.getId(), newRecipe.getName(), newRecipe.getImageurl(), newRecipe.getDifficulty(), newRecipe.getDesc(), newRecipe.getTime(), newRecipe.getMethod(), newRecipe.getType(), listing, riList);
        boolean edited = recipeConn.editRecipe(newRecipeFull);
        return edited;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taMethod = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        taDescription = new javax.swing.JTextArea();
        txtCategory = new javax.swing.JTextField();
        cbDifficulty = new javax.swing.JComboBox<>();
        txtImageURL = new javax.swing.JTextField();
        txtRecipeName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblDifficulty = new javax.swing.JLabel();
        lblImageURL = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pIngredients = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cbIngredientNames = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        liIngredients = new javax.swing.JList<>();
        btnAddToList = new javax.swing.JButton();
        btnRemoveFromList = new javax.swing.JButton();
        lblIngredients = new javax.swing.JLabel();
        btnAddRecipe = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtTime = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        lblError = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        taMethod.setColumns(20);
        taMethod.setRows(5);
        taMethod.setNextFocusableComponent(cbIngredientNames);
        jScrollPane1.setViewportView(taMethod);

        taDescription.setColumns(20);
        taDescription.setRows(5);
        taDescription.setNextFocusableComponent(taMethod);
        jScrollPane2.setViewportView(taDescription);

        txtCategory.setNextFocusableComponent(taDescription);

        cbDifficulty.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        cbDifficulty.setNextFocusableComponent(txtTime);

        txtImageURL.setNextFocusableComponent(cbDifficulty);

        txtRecipeName.setFocusCycleRoot(true);
        txtRecipeName.setNextFocusableComponent(txtImageURL);

        jLabel5.setText("Time Required (minutes):");

        jLabel3.setText("Method:");

        jLabel2.setText("Description:");

        jLabel4.setText("Category:");

        lblDifficulty.setText("Difficulty:");

        lblImageURL.setText("Image URL (optional):");

        jLabel1.setText("Recipe Name:");

        pIngredients.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingredients"));

        jLabel7.setText("Name:");

        cbIngredientNames.setEditable(true);
        cbIngredientNames.setNextFocusableComponent(txtQuantity);

        jLabel6.setText("Quantity:");

        txtQuantity.setNextFocusableComponent(btnAddToList);

        jScrollPane3.setViewportView(liIngredients);

        btnAddToList.setText("Add to list");
        btnAddToList.setNextFocusableComponent(btnRemoveFromList);
        btnAddToList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToListActionPerformed(evt);
            }
        });

        btnRemoveFromList.setText("Remove from list");
        btnRemoveFromList.setNextFocusableComponent(btnAddRecipe);
        btnRemoveFromList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveFromListActionPerformed(evt);
            }
        });

        lblIngredients.setText(" ");

        javax.swing.GroupLayout pIngredientsLayout = new javax.swing.GroupLayout(pIngredients);
        pIngredients.setLayout(pIngredientsLayout);
        pIngredientsLayout.setHorizontalGroup(
            pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIngredientsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIngredients)
                    .addGroup(pIngredientsLayout.createSequentialGroup()
                        .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtQuantity)
                            .addComponent(cbIngredientNames, 0, 216, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRemoveFromList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddToList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        pIngredientsLayout.setVerticalGroup(
            pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIngredientsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbIngredientNames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(btnAddToList))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pIngredientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemoveFromList)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(4, 4, 4)
                .addComponent(lblIngredients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnAddRecipe.setText("Save Recipe");
        btnAddRecipe.setNextFocusableComponent(btnCancel);
        btnAddRecipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRecipeActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.setNextFocusableComponent(txtRecipeName);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtTime.setNextFocusableComponent(txtCategory);

        lblError.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDifficulty)
                    .addComponent(lblImageURL)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbDifficulty, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImageURL)
                    .addComponent(txtRecipeName))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(pIngredients, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(192, 192, 192)
                        .addComponent(lblError)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddRecipe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtRecipeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblImageURL)
                            .addComponent(txtImageURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDifficulty)
                            .addComponent(cbDifficulty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pIngredients, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblError)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnAddRecipe))
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Segoe Print", 0, 24)); // NOI18N
        jLabel8.setText("Food Made Easy");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(337, 337, 337)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAddToListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToListActionPerformed
        addToList();
    }//GEN-LAST:event_btnAddToListActionPerformed

    private void btnRemoveFromListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveFromListActionPerformed
        if(!ingredientsModel.isEmpty()){
            //if nothing has been selected but button is hit, remove top ingredient
            if(liIngredients.isSelectionEmpty()){
                //split string to get name and quantity components
                String full = ingredientsModel.getElementAt(0).toString();
                String[] parts = full.split("      -      Quantity: ");
                String name = parts[0];
                String quantity = parts[1];
                ingredientDetails.remove(name, quantity);
                ingredientsModel.remove(0);
            }else{
                //otherwise remove selected ingredient
                String full = liIngredients.getSelectedValue();
                String[] parts = full.split("      -      Quantity: ");
                String name = parts[0];
                String quantity = parts[1];
                ingredientDetails.remove(name, quantity);
                ingredientsModel.remove(liIngredients.getSelectedIndex());
            }
            liIngredients.setModel(ingredientsModel);
            lblIngredients.setText(" ");
        }
    }//GEN-LAST:event_btnRemoveFromListActionPerformed

    private void btnAddRecipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRecipeActionPerformed
        //check if the required fields contain valid values
        if(txtRecipeName.getText().isEmpty()){
            lblError.setText("Please enter a recipe name");
        }else if(txtCategory.getText().isEmpty()){
            lblError.setText("Please enter a category");
        }else if(txtTime.getText().isEmpty()){
            lblError.setText("Please enter an approximate time");
        }else if(taDescription.getText().isEmpty()){
            lblError.setText("Please enter a description");
        }else if(taMethod.getText().isEmpty()){
            lblError.setText("Please enter a method");
        }else if(ingredientDetails.isEmpty()){
            lblError.setText("Please enter a list of ingredients");
        }else{
            //check if time is an integer
            try{
                Integer.parseInt(txtTime.getText());
                String ingredString = "";//build a string of the ingredients
                ingredString = ingredientDetails.entrySet().stream().map((entry) -> "\r\n        -" + entry.getKey() + " (" + entry.getValue() + ")").reduce(ingredString, String::concat);
                if(edit){
                    int dialogResult = JOptionPane.showConfirmDialog(this, "You are about to change the recipe to have the details:\r\n\nName: " + txtRecipeName.getText() + "\r\nDifficulty: " + cbDifficulty.getSelectedItem().toString() + "/10\r\nCategory: " + txtCategory.getText() + "\r\nIngredients:" + ingredString + "\r\n\r\nAre you sure you wish to save the changes to this recipe?", "Edit Recipe", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == 0) {//yes
                        ArrayList<FullRecipeDetails> allRecipes = recipeConn.getAllRecipesIngredients();
                        for(int i = 0; i < allRecipes.size(); i++){
                            //if the name is the same as another recipe and the id is the same, then we are looking at the recipe being edited,
                            //otherwise if the id is different then we're looking at a different recipe and shouldn't be allowed to have the same name
                            if(txtRecipeName.getText().equalsIgnoreCase(allRecipes.get(i).getRecipeName()) && allRecipes.get(i).getRecipeId() != editID){
                                JOptionPane.showMessageDialog(this, "The recipe '" + txtRecipeName.getText() + "' already exists, please either change the recipe name or edit the recipe by selecting it from the list on the control panel.", "Recipe already exists", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        boolean result = editTheRecipe();
                        if(result){
                            this.dispose();
                        }else{
                            JOptionPane.showMessageDialog(this, "Something went wrong when trying to edit the recipe: " + txtRecipeName.getText() + "\r\n\nPlease try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else{
                    int dialogResult = JOptionPane.showConfirmDialog(this, "You are about to add the following recipe:\r\n\nName: " + txtRecipeName.getText() + "\r\nDifficulty: " + cbDifficulty.getSelectedItem().toString() + "/10\r\nCategory: " + txtCategory.getText() + "\r\nIngredients:" + ingredString + "\r\n\r\nAre you sure you wish to add this recipe?", "Add Recipe", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == 0) {//yes
                        ArrayList<FullRecipeDetails> allRecipes = recipeConn.getAllRecipesIngredients();
                        for(int i = 0; i < allRecipes.size(); i++){
                            if(txtRecipeName.getText().equalsIgnoreCase(allRecipes.get(i).getRecipeName())){
                                JOptionPane.showMessageDialog(this, "The recipe '" + txtRecipeName.getText() + "' already exists, please either change the recipe name or edit the recipe by selecting it from the list on the control panel.", "Recipe already exists", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        boolean result = addTheRecipe();
                        if(result){
                            int dialogResultAdded = JOptionPane.showConfirmDialog(this, "The recipe named: " + txtRecipeName.getText() + ",\r\n has been added successfully. \r\n\nDo you wish to add another recipe?", "Success", JOptionPane.YES_NO_OPTION);
                            if(dialogResultAdded == 0) {//yes
                                setup();
                            }else{//no
                                this.dispose();
                            }
                        }else{
                            JOptionPane.showMessageDialog(this, "Something went wrong when trying to add the recipe: " + txtRecipeName.getText() + "\r\n\nPlease try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                lblError.setText(" ");
                System.out.println("Everything's shiny Cap'n");
            } catch (NumberFormatException e) {
                lblError.setText("Please enter a number value for the time");
            }
        }
    }//GEN-LAST:event_btnAddRecipeActionPerformed

    
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
            java.util.logging.Logger.getLogger(AddRecipe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddRecipe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddRecipe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddRecipe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddRecipe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddRecipe;
    private javax.swing.JButton btnAddToList;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRemoveFromList;
    private javax.swing.JComboBox<String> cbDifficulty;
    private javax.swing.JComboBox<String> cbIngredientNames;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblDifficulty;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblImageURL;
    private javax.swing.JLabel lblIngredients;
    private javax.swing.JList<String> liIngredients;
    private javax.swing.JPanel pIngredients;
    private javax.swing.JTextArea taDescription;
    private javax.swing.JTextArea taMethod;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtImageURL;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtRecipeName;
    private javax.swing.JTextField txtTime;
    // End of variables declaration//GEN-END:variables
}
