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
import foodmadeeasy.generic.FullRecipeDetails;
import foodmadeeasy.generic.Ingredient;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author chris
 */
public class AdminControlPanel extends javax.swing.JFrame {

    private DefaultListModel adminListModel;
    private DefaultListModel ingredientListModel;
    private DefaultListModel recipeListModel;
    private DefaultComboBoxModel cbSortModel;
    
    private ArrayList<AdminUser> admins;
    private ArrayList<Ingredient> allIngredients;
    private ArrayList<FullRecipeDetails> allRecipes;
    
    private String adminUsername;
    
    private ConnectionHandlerAdmin adminConnection = new ConnectionHandlerAdmin();
    private ConnectionHandlerIngredient ingredientHandler = new ConnectionHandlerIngredient();
    private ConnectionHandlerRecipeIngredient recipes = new ConnectionHandlerRecipeIngredient();
    
    /**
     * Creates new form AdminControlPanel
     */
    public AdminControlPanel() {
        initComponents();
    }
    
    /**
     * Creates new form AdminControlPanel
     * @param username
     */
    public AdminControlPanel(String username) {
        initComponents();
        this.adminUsername = username;
        
        //fill out sorting combo boxes
        String[] sortNames = new String[] {"Name: A-Z", "Name: Z-A"};
        //set the models for the combo boxes and force updates
        cbSortModel = new DefaultComboBoxModel(sortNames);
        cbSortAdmins.setModel(cbSortModel);
        cbSortRecipes.setModel(cbSortModel);
        cbSortIngredients.setModel(cbSortModel);
        
        createListeners();
        
        setup();
    }

    private void setup(){
        Container a = this.getContentPane();
        a.setBackground(Color.white);
        
        adminListModel = new DefaultListModel();
        ingredientListModel = new DefaultListModel();
        recipeListModel = new DefaultListModel();
        
        //populate the list for admins
        admins = adminConnection.getAdmin(null);
        for(int i = 0; i < admins.size(); i++){
            adminListModel.addElement(admins.get(i).getUsername());
        }
        liAdminNames.setModel(adminListModel);
        //force sort
        cbSortAdmins.setSelectedIndex(cbSortAdmins.getSelectedIndex());
        
        //populate list for ingredients
        allIngredients = ingredientHandler.getAllIngredients();
        for(int i = 0; i < allIngredients.size(); i++){
            ingredientListModel.addElement(allIngredients.get(i).getName());
        }
        liIngredientNames.setModel(ingredientListModel);
        //force sort
        cbSortIngredients.setSelectedIndex(cbSortIngredients.getSelectedIndex());
        
        //populate list for recipes
        allRecipes = recipes.getAllRecipesIngredients();
        for(int i = 0; i < allRecipes.size(); i++){
            recipeListModel.addElement(allRecipes.get(i).getRecipeName());
        }
        liRecipeNames.setModel(recipeListModel);
        //force sort
        cbSortRecipes.setSelectedIndex(cbSortRecipes.getSelectedIndex());
    }
    
    private void createListeners(){
        cbSortAdmins.addActionListener ((ActionEvent e) -> {
            ArrayList<String> sort = new ArrayList();
            //add contents of model to arraylist
            for(int i = 0; i < adminListModel.getSize(); i++){
                sort.add(adminListModel.getElementAt(i).toString());
            }
            //sort the list according to the action selected
            switch(cbSortAdmins.getSelectedIndex()){
                case 0: 
                        Collections.sort(sort);
                        break;
                case 1: 
                        Collections.sort(sort, Collections.reverseOrder());
                        break;
            }
            //add contents back into model
            
            adminListModel = new DefaultListModel();
            for(int i = 0; i < sort.size(); i++){
                adminListModel.addElement(sort.get(i));
                System.out.println(sort.get(i));
            }
            liAdminNames.setModel(adminListModel);
        });
        
        cbSortRecipes.addActionListener ((ActionEvent e) -> {
            ArrayList<String> sort = new ArrayList();
            //add contents of model to arraylist
            for(int i = 0; i < recipeListModel.getSize(); i++){
                sort.add(recipeListModel.getElementAt(i).toString());
            }
            //sort the list according to the action selected
            switch(cbSortRecipes.getSelectedIndex()){
                case 0: 
                        Collections.sort(sort);
                        break;
                case 1: 
                        Collections.sort(sort, Collections.reverseOrder());
                        break;
            }
            //add contents back into model
            
            recipeListModel = new DefaultListModel();
            for(int i = 0; i < sort.size(); i++){
                recipeListModel.addElement(sort.get(i));
                System.out.println(sort.get(i));
            }
            liRecipeNames.setModel(recipeListModel);
        });
        
        cbSortIngredients.addActionListener ((ActionEvent e) -> {
            ArrayList<String> sort = new ArrayList();
            //add contents of model to arraylist
            for(int i = 0; i < ingredientListModel.getSize(); i++){
                sort.add(ingredientListModel.getElementAt(i).toString());
            }
            //sort the list according to the action selected
            switch(cbSortIngredients.getSelectedIndex()){
                case 0: 
                        Collections.sort(sort);
                        break;
                case 1: 
                        Collections.sort(sort, Collections.reverseOrder());
                        break;
            }
            //add contents back into model
            
            ingredientListModel = new DefaultListModel();
            for(int i = 0; i < sort.size(); i++){
                ingredientListModel.addElement(sort.get(i));
                System.out.println(sort.get(i));
            }
            liIngredientNames.setModel(ingredientListModel);
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tpAdmin = new javax.swing.JTabbedPane();
        pAdminControl = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        liAdminNames = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        btnAddUser = new javax.swing.JButton();
        btnEditUser = new javax.swing.JButton();
        btnRemoveUser = new javax.swing.JButton();
        lblErrorAdmin = new javax.swing.JLabel();
        cbSortAdmins = new javax.swing.JComboBox<>();
        pRecipe = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        liRecipeNames = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        btnAddRecipe = new javax.swing.JButton();
        btnEditRecipe = new javax.swing.JButton();
        btnDeleteRecipe = new javax.swing.JButton();
        lblError = new javax.swing.JLabel();
        cbSortRecipes = new javax.swing.JComboBox<>();
        pIngredient = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        liIngredientNames = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        btnAddIngredient = new javax.swing.JButton();
        btnEditIngredient = new javax.swing.JButton();
        btnDeleteIngredient = new javax.swing.JButton();
        lblErrorIngredient = new javax.swing.JLabel();
        cbSortIngredients = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Control Panel");

        tpAdmin.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setViewportView(liAdminNames);

        jLabel2.setText("All Users:");

        btnAddUser.setText("Add New User");
        btnAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUserActionPerformed(evt);
            }
        });

        btnEditUser.setText("Edit User Details");
        btnEditUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditUserActionPerformed(evt);
            }
        });

        btnRemoveUser.setText("Delete User");
        btnRemoveUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveUserActionPerformed(evt);
            }
        });

        lblErrorAdmin.setText(" ");

        javax.swing.GroupLayout pAdminControlLayout = new javax.swing.GroupLayout(pAdminControl);
        pAdminControl.setLayout(pAdminControlLayout);
        pAdminControlLayout.setHorizontalGroup(
            pAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAdminControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pAdminControlLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbSortAdmins, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(87, 87, 87)
                .addGroup(pAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnAddUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRemoveUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblErrorAdmin))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        pAdminControlLayout.setVerticalGroup(
            pAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAdminControlLayout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(pAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pAdminControlLayout.createSequentialGroup()
                        .addGroup(pAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbSortAdmins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pAdminControlLayout.createSequentialGroup()
                        .addComponent(lblErrorAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveUser)
                        .addGap(99, 99, 99))))
        );

        tpAdmin.addTab("Manage Users", pAdminControl);

        jScrollPane2.setViewportView(liRecipeNames);

        jLabel3.setText("All Recipes:");

        btnAddRecipe.setText("Add Recipe");
        btnAddRecipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRecipeActionPerformed(evt);
            }
        });

        btnEditRecipe.setText("Edit Recipe");
        btnEditRecipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditRecipeActionPerformed(evt);
            }
        });

        btnDeleteRecipe.setText("Delete Recipe");
        btnDeleteRecipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteRecipeActionPerformed(evt);
            }
        });

        lblError.setText(" ");

        javax.swing.GroupLayout pRecipeLayout = new javax.swing.GroupLayout(pRecipe);
        pRecipe.setLayout(pRecipeLayout);
        pRecipeLayout.setHorizontalGroup(
            pRecipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRecipeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pRecipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pRecipeLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbSortRecipes, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(96, 96, 96)
                .addGroup(pRecipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblError)
                    .addGroup(pRecipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnAddRecipe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditRecipe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeleteRecipe)))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        pRecipeLayout.setVerticalGroup(
            pRecipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pRecipeLayout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(pRecipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbSortRecipes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(pRecipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pRecipeLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(lblError)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddRecipe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditRecipe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteRecipe))
                    .addGroup(pRecipeLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        tpAdmin.addTab("Manage Recipes", pRecipe);

        jScrollPane3.setViewportView(liIngredientNames);

        jLabel4.setText("All Ingredients:");

        btnAddIngredient.setText("Add Ingredient");
        btnAddIngredient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddIngredientActionPerformed(evt);
            }
        });

        btnEditIngredient.setText("Edit Ingredient");
        btnEditIngredient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditIngredientActionPerformed(evt);
            }
        });

        btnDeleteIngredient.setText("Delete Ingredient");
        btnDeleteIngredient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteIngredientActionPerformed(evt);
            }
        });

        lblErrorIngredient.setText(" ");

        javax.swing.GroupLayout pIngredientLayout = new javax.swing.GroupLayout(pIngredient);
        pIngredient.setLayout(pIngredientLayout);
        pIngredientLayout.setHorizontalGroup(
            pIngredientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIngredientLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pIngredientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pIngredientLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbSortIngredients, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88)
                .addGroup(pIngredientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorIngredient)
                    .addGroup(pIngredientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnAddIngredient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditIngredient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeleteIngredient)))
                .addContainerGap(103, Short.MAX_VALUE))
        );
        pIngredientLayout.setVerticalGroup(
            pIngredientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIngredientLayout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(pIngredientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbSortIngredients, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pIngredientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pIngredientLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pIngredientLayout.createSequentialGroup()
                        .addComponent(lblErrorIngredient)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddIngredient)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditIngredient)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteIngredient)
                        .addGap(99, 99, 99))))
        );

        tpAdmin.addTab("Manage Ingredients", pIngredient);

        jLabel1.setFont(new java.awt.Font("Segoe Print", 0, 24)); // NOI18N
        jLabel1.setText("Food Made Easy");

        btnLogout.setText("Log out");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpAdmin)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(122, 122, 122)
                .addComponent(btnLogout)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(btnLogout))
                .addGap(18, 18, 18)
                .addComponent(tpAdmin))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you wish to log out?", "Log out?", JOptionPane.YES_NO_OPTION);
        if(dialogResult == 0) {//yes
            JFrame mainPage = new MainPage();
            mainPage.setLocationRelativeTo(null);
            mainPage.setResizable(false);
            mainPage.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            mainPage.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnAddRecipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRecipeActionPerformed
        JFrame addRecipe = new AddRecipe(false, null);
        addRecipe.setLocationRelativeTo(null);
        addRecipe.setResizable(false);
        addRecipe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addRecipe.setVisible(true);
        
        //need this listener with the other one as otherwise when the x is clicked
        //nothing happens ¯\_(ツ)_/¯
        addRecipe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                addRecipe.dispose();
            }
        });
        
        //add listener so lists can be refreshed when form is closed
        addRecipe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                setup();
                System.out.println("Hello from the button");
                addRecipe.dispose();
            }
        });
    }//GEN-LAST:event_btnAddRecipeActionPerformed

    private void btnEditRecipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditRecipeActionPerformed
        FullRecipeDetails selectedRecipe = null;
        if(liRecipeNames.getSelectedValue() == null){
            lblError.setText("Please select a recipe to edit");
        }else{
            lblError.setText(" ");
            for(int i = 0; i < allRecipes.size(); i++){
                if(liRecipeNames.getSelectedValue().equalsIgnoreCase(allRecipes.get(i).getRecipeName())){
                    selectedRecipe = allRecipes.get(i);
                }
            }
            JFrame addRecipe = new AddRecipe(true, selectedRecipe);//edit a recipe rather than adding one
            addRecipe.setLocationRelativeTo(null);
            addRecipe.setResizable(false);
            addRecipe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addRecipe.setVisible(true);

            //need this listener with the other one as otherwise when the x is clicked
            //nothing happens ¯\_(ツ)_/¯
            addRecipe.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent event) {
                    addRecipe.dispose();
                }
            });

            //add listener so lists can be refreshed when form is closed
            addRecipe.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent event) {
                    setup();
                    System.out.println("Hello from the button");
                    addRecipe.dispose();
                }
            });
        }
    }//GEN-LAST:event_btnEditRecipeActionPerformed

    private void btnDeleteRecipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteRecipeActionPerformed
        FullRecipeDetails selectedRecipe = null;
        if(liRecipeNames.getSelectedValue() == null){
            lblError.setText("Please select a recipe to delete");
        }else{
            lblError.setText(" ");
            for(int i = 0; i < allRecipes.size(); i++){
                if(liRecipeNames.getSelectedValue().equalsIgnoreCase(allRecipes.get(i).getRecipeName())){
                    selectedRecipe = allRecipes.get(i);
                }
            }
            //confirm with the user that they wish to delete this recipe
            int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete the recipe: " + selectedRecipe.getRecipeName() + "?", "Delete Recipe", JOptionPane.YES_NO_OPTION);
            if(dialogResult == 0) {//yes
                System.out.println(recipes.deleteRecipe(selectedRecipe.getRecipeId()));
                setup();
            }
        }
    }//GEN-LAST:event_btnDeleteRecipeActionPerformed

    private void btnAddIngredientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddIngredientActionPerformed
        JFrame addIngredient = new AddIngredient(false, null);
        addIngredient.setLocationRelativeTo(null);
        addIngredient.setResizable(false);
        addIngredient.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addIngredient.setVisible(true);
        
        //need this listener with the other one as otherwise when the x is clicked
        //nothing happens ¯\_(ツ)_/¯
        addIngredient.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                addIngredient.dispose();
            }
        });
        
        //add listener so lists can be refreshed when form is closed
        addIngredient.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                setup();
                System.out.println("Hello from the button");
                addIngredient.dispose();
            }
        });
    }//GEN-LAST:event_btnAddIngredientActionPerformed

    private void btnEditIngredientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditIngredientActionPerformed
        Ingredient selectedIngredient = null;
        if(liIngredientNames.getSelectedValue() == null){
            lblErrorIngredient.setText("Please select an ingredient to edit");
        }else{
            lblErrorIngredient.setText(" ");
            for(int i = 0; i < allIngredients.size(); i++){
                if(liIngredientNames.getSelectedValue().equalsIgnoreCase(allIngredients.get(i).getName())){
                    selectedIngredient = allIngredients.get(i);
                }
            }
            JFrame addIngredient = new AddIngredient(true, selectedIngredient);//edit an ingredient rather than adding one
            addIngredient.setLocationRelativeTo(null);
            addIngredient.setResizable(false);
            addIngredient.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addIngredient.setVisible(true);

            //need this listener with the other one as otherwise when the x is clicked
            //nothing happens ¯\_(ツ)_/¯
            addIngredient.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent event) {
                    addIngredient.dispose();
                }
            });

            //add listener so lists can be refreshed when form is closed
            addIngredient.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent event) {
                    setup();
                    System.out.println("Hello from the button");
                    addIngredient.dispose();
                }
            });
        }
    }//GEN-LAST:event_btnEditIngredientActionPerformed

    private void btnDeleteIngredientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteIngredientActionPerformed
        Ingredient selectedIngredient = null;
        if(liIngredientNames.getSelectedValue() == null){
            lblErrorIngredient.setText("Please select an ingredient to delete");
        }else{
            lblErrorIngredient.setText(" ");
            //set the object for the selected ingredient
            for(int i = 0; i < allIngredients.size(); i++){
                if(liIngredientNames.getSelectedValue().equalsIgnoreCase(allIngredients.get(i).getName())){
                    selectedIngredient = allIngredients.get(i);
                }
            }
            
            ArrayList<String> linkedRecipes = new ArrayList();
            if(selectedIngredient != null){
                //check to see if the ingredient is being used by any recipes
                //loop through all recipes
                for(int i = 0; i < allRecipes.size(); i++){
                    //if it is actually a recipe
                    if(allRecipes.get(i) != null){
                        //for every ingredient, add to map
                        for(int j = 0; j < allRecipes.get(i).getIngredients().size(); j++){
                            //if the current recipe contains an ingredient with this id, add to list
                            if(selectedIngredient.getId() == allRecipes.get(i).getIngredients().get(j).getId()){
                                linkedRecipes.add(allRecipes.get(i).getRecipeName());
                            }
                        }
                    }
                }
                //if the ingredient isn't linked to any recipes, feel free to delete
                if(linkedRecipes.isEmpty()){
                    //confirm with the user that they wish to delete this recipe
                    int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete the ingredient: " + selectedIngredient.getName() + "?", "Delete Ingredient", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == 0) {//yes
                        System.out.println("Ingredient deleted? : "+ingredientHandler.deleteIngredient(selectedIngredient.getId()));
                        setup();
                    }
                }else{
                    String recString = "";//build a string of the recipes
                    for(int i = 0; i < linkedRecipes.size(); i++){
                        recString += "\r\n        -" + linkedRecipes.get(i);
                    }
                    JOptionPane.showMessageDialog(this, "This ingredient is unable to be deleted while it is still linked to the following recipe(s):\r\n\n" + recString + "\r\n\nPlease try again later.", "Unable to delete ingredient", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnDeleteIngredientActionPerformed

    private void btnAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUserActionPerformed
        JFrame addUser = new AddUser(false, null);
        addUser.setLocationRelativeTo(null);
        addUser.setResizable(false);
        addUser.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addUser.setVisible(true);
        
        //need this listener with the other one as otherwise when the x is clicked
        //nothing happens ¯\_(ツ)_/¯
        addUser.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                addUser.dispose();
            }
        });
        
        //add listener so lists can be refreshed when form is closed
        addUser.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                setup();
                System.out.println("Hello from the button");
                addUser.dispose();
            }
        });
    }//GEN-LAST:event_btnAddUserActionPerformed

    private void btnEditUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditUserActionPerformed
        AdminUser adminUser = null;
        if(liAdminNames.getSelectedValue() == null){
            lblErrorAdmin.setText("Please select a user to edit");
        }else{
            lblErrorAdmin.setText(" ");
            for(int i = 0; i < admins.size(); i++){
                if(liAdminNames.getSelectedValue().equalsIgnoreCase(admins.get(i).getUsername())){
                    adminUser = admins.get(i);
                }
            }
            if(adminUser != null){//just to stop possible null reference
                JFrame addUser = new AddUser(true, adminUser.getUsername());//edit a user rather than adding one
                addUser.setLocationRelativeTo(null);
                addUser.setResizable(false);
                addUser.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                addUser.setVisible(true);

                //need this listener with the other one as otherwise when the x is clicked
                //nothing happens ¯\_(ツ)_/¯
                addUser.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent event) {
                        addUser.dispose();
                    }
                });

                //add listener so lists can be refreshed when form is closed
                addUser.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent event) {
                        setup();
                        System.out.println("Hello from the button");
                        addUser.dispose();
                    }
                });
            }
        }
    }//GEN-LAST:event_btnEditUserActionPerformed

    private void btnRemoveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveUserActionPerformed
        AdminUser selectedAdmin = null;
        if(liAdminNames.getSelectedValue() == null){
            lblErrorAdmin.setText("Please select a user to delete");
        }else if(adminListModel.size() > 1){
            lblErrorAdmin.setText(" ");
            for(int i = 0; i < admins.size(); i++){
                if(liAdminNames.getSelectedValue().equalsIgnoreCase(admins.get(i).getUsername())){
                    selectedAdmin = admins.get(i);
                }
            }
            if(selectedAdmin!=null){//just to reeeeaaaly make sure it isn't null, you can't be too safe
                //if the user is trying to delete the account they logged in with:
                if(selectedAdmin.getUsername().equals(adminUsername)){
                    //ask if they are sure they want to do this
                    int dialogResult = JOptionPane.showConfirmDialog(this, "You are currently logged in as the user that you're trying to delete ("+ adminUsername +"),\r\nIf you continue you will be logged out and will no longer be able to log in as this user.\r\n\nAre you sure you wish to delete this user?", "Delete User", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == 0) {//yes
                        System.out.println(adminConnection.deleteAdmin(selectedAdmin.getUsername()));
                        JFrame mainPage = new MainPage();
                        mainPage.setLocationRelativeTo(null);
                        mainPage.setResizable(false);
                        mainPage.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                        mainPage.setVisible(true);
                        this.dispose();
                    }
                }else{
                    //otherwise ask if they want to delete the user which they aren't
                    int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete the user: " + selectedAdmin.getUsername() + "?", "Delete User", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == 0) {//yes
                        System.out.println(adminConnection.deleteAdmin(selectedAdmin.getUsername()));
                        setup();
                    }
                }
            }
        }else{//if the user is the only admin, don't allow them to lock everyone out
            JOptionPane.showMessageDialog(this, "You are the only admin, to prevent locking everyone out of the system: Permission Denied", "Unable to delete user", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRemoveUserActionPerformed

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
            java.util.logging.Logger.getLogger(AdminControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminControlPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddIngredient;
    private javax.swing.JButton btnAddRecipe;
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnDeleteIngredient;
    private javax.swing.JButton btnDeleteRecipe;
    private javax.swing.JButton btnEditIngredient;
    private javax.swing.JButton btnEditRecipe;
    private javax.swing.JButton btnEditUser;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRemoveUser;
    private javax.swing.JComboBox<String> cbSortAdmins;
    private javax.swing.JComboBox<String> cbSortIngredients;
    private javax.swing.JComboBox<String> cbSortRecipes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblErrorAdmin;
    private javax.swing.JLabel lblErrorIngredient;
    private javax.swing.JList<String> liAdminNames;
    private javax.swing.JList<String> liIngredientNames;
    private javax.swing.JList<String> liRecipeNames;
    private javax.swing.JPanel pAdminControl;
    private javax.swing.JPanel pIngredient;
    private javax.swing.JPanel pRecipe;
    private javax.swing.JTabbedPane tpAdmin;
    // End of variables declaration//GEN-END:variables
}
