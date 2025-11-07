# MVC-AbstractFactory_Composite_Command-JoseSoto-MiguelGuerra

Descripcion:
Sistema de Punto de Venta (POS) para gestionar productos, ventas, clientes, proveedores, inventario y usuarios. Permite acciones como agregar productos al carrito, eliminar, aplicar descuentos, cobrar y generar facturas. Está diseñado para implementarse en Java (JavaFX) con una base de datos relaciona (MySQL).

Jose Gil Soto Mendez y Miguel Guerra Negrete

Lenguaje: Java.

herramientas:
▪ Frameworks/librerías: JavaFX (para GUI) solo por ahora.
▪ Base de datos: MySQL.
▪ Herramientas: Maven, Git, PlantUML para los diagramas.
▪ Estilo de código: Google Java Style Guide.

▪ Abstract Factory (Modelo): Provee familias de DAOs (ProductDAO, SaleDAO, CustomerDAO, UserDAO, SupplierDAO, InventoryDAO). El servicio de negocio solicita DAOs a la fábrica sin acoplarse al motor de BD concreto. Facilita pruebas y portabilidad.

▪ Composite (Vista): Los componentes de UI (Panel, CatalogPanel, CartPanel, Button, Table) se implementan como una jerarquía donde contenedores y hojas son tratados uniformemente. Simplifica construcción dinámica y reusabilidad de secciones de UI.

▪ Command (Controlador): Cada operación del usuario se modela como un Command con execute() y undo(). El Controller (o CommandInvoker) crea y ejecuta comandos, mantiene historial y coordina transacciones con el Modelo. Esto desacopla la lógica de la UI de la lógica de negocio.

▪ Observer (Extra): El Modelo notifica a vistas (o ViewModels) cuando cambian datos importantes (p. ej. stock). Implementar Observer evita polling y mantiene la UI sincronizada.

▪ Strategy (Extra): Para cálculos configurables (descuentos, reglas fiscales) se definen estrategias intercambiables en tiempo de ejecución.
