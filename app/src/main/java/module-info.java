
module proyectito.rapido {
    requires transitive java.desktop;
    requires java.logging;

    exports proyectito.rapido;
    exports proyectito.rapido.view;
    exports proyectito.rapido.controller;
    exports proyectito.rapido.model;
}