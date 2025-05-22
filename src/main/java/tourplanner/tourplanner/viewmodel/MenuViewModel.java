package tourplanner.tourplanner.viewmodel;

import tourplanner.tourplanner.model.Tour;
import tourplanner.tourplanner.service.TourService;

import java.util.function.Consumer;

public class MenuViewModel {
    private final TourService service;
    private Consumer<TourViewModel> onCreate, onEdit, onDelete;
    private Consumer<String> onFind;
    private Runnable onReload;

    public MenuViewModel(TourService svc) {
        this.service = svc;
    }

    public void setOnCreate(Consumer<TourViewModel> c) {
        onCreate = c;
    }

    public void setOnEdit(Consumer<TourViewModel> c) {
        onEdit = c;
    }

    public void setOnDelete(Consumer<TourViewModel> c) {
        onDelete = c;
    }

    public void setOnFind(Consumer<String> c) {
        onFind = c;
    }

    public void setOnReload(Runnable r) {
        onReload = r;
    }

    public void createTour() {
        // Erzeuge neue Demo-Tour mit leeren Feldern; imagePath="" → VM setzt demo.png
        Tour m = new Tour(
                "Neue Tour",      // name
                "",               // description
                "Startort",       // from
                "Zielort",        // to
                "Auto",           // transportType
                0.0,              // distance
                "",               // estimatedTime
                ""                // imagePath: leer, führt zum Placeholder
        );
        service.addTour(m);
        var tvm = new TourViewModel(m);
        if (onCreate != null) onCreate.accept(tvm);
    }

    public void editTour(TourViewModel tvm) {
        tvm.nameProperty().set(tvm.nameProperty().get() + " (geändert)");
        if (onEdit != null) onEdit.accept(tvm);
    }

    public void deleteTour(TourViewModel tvm) {
        service.removeTour(tvm.toModel());
        if (onDelete != null) onDelete.accept(tvm);
    }

    public void findTour() {
        String q = "Stadt";
        if (onFind != null) onFind.accept(q);
    }

    public void reloadTours() {
        if (onReload != null) onReload.run();
    }
}
