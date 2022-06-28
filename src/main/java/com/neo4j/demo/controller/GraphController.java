package com.neo4j.demo.controller;

import com.neo4j.demo.model.Customer;
import com.neo4j.demo.model.MovieEntity;
import com.neo4j.demo.service.GraphService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class GraphController {
    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

//    @GetMapping("/")
    public String index() {
        return "Welcome to Frank's Demo";
    }

    @GetMapping("/nodes")
    public int nodeCount() {
        return graphService.getNodes();
    }

    @GetMapping("/nodes/{label}")
    public int nodeCountByLabel(@PathVariable("label") String label) {
        return graphService.getNodes(label);
    }

    @GetMapping("/titles")
    public Stream<String> getTitles() {
        return graphService.titles();
    }

    @GetMapping(value = "/movies",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getMovies(){
        return graphService.movies().map(MovieEntity::getTitle).delayElements(Duration.ofSeconds(1));
    }

    @GetMapping("/triangle")
    public List<Customer[]> getTriangle() {
        return graphService.getTriangle();
    }
}
