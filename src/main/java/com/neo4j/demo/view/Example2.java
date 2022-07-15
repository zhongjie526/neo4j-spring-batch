package com.neo4j.demo.view;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.CustomerList;
import com.neo4j.demo.service.GraphService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
@PageTitle("Example 2")
@Route(value = "/example2")
public class Example2 extends VerticalLayout {


    private  GraphService graphService;

    public Example2(GraphService graphService) {
        this.graphService = graphService;

        RouterLink back = new RouterLink("Home", MainView.class);

//        Checkbox checkbox = new Checkbox();
//        checkbox.setLabel("I accept the terms and conditions");

        add(back);

        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
        checkboxGroup.setLabel("Pick favourite movies");
        checkboxGroup.setItems("Island Exorcist", "Oz Liaisons", "Sweethearts Suspects", "Insects Stone","Secrets Paradise");
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        add(checkboxGroup);



        Button buttonShow = new Button("Show Recommendations");
        Button buttonReset = new Button("Reset");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(buttonShow);
        buttonLayout.add(buttonReset);

        add(buttonLayout);

        Grid<String> grid = new Grid<>();
        grid.setSizeFull();
        grid.addColumn(String::toString).setHeader("Movie");
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(10, grid);
        content.setSizeFull();
        buttonShow.addClickListener(buttonClickEvent -> {
            Set<String> selectedMovies = checkboxGroup.getSelectedItems();
            if(selectedMovies.isEmpty()){
                log.info("Selection is Empty");
            } else{
                selectedMovies.forEach(graphService::addMovie);
                List<String> neighbors = graphService.getNeighbors("999999",25);
                List<String> recommendations = graphService.getRecommendations("999999", neighbors, 10);
                recommendations.forEach(System.out::println);

                add(content);
                setSizeFull();
                grid.setItems(recommendations);
            }
        });



        buttonReset.addClickListener(buttonClickEvent -> {
           graphService.deleteFrank();
           graphService.createFrank();
           checkboxGroup.clear();
           grid.setItems();
        });



    }



}