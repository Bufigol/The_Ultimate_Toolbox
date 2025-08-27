# Documentación de Utilidades Matemáticas

## Utilidades Matemáticas en The_Ultimate_Toolbox

### Introducción

El módulo de utilidades matemáticas de The_Ultimate_Toolbox proporciona un conjunto completo de herramientas para realizar cálculos matemáticos, estadísticos y conversiones de unidades comunes en aplicaciones Java. Este módulo está diseñado para simplificar operaciones matemáticas frecuentes, reduciendo la cantidad de código necesario y mejorando la legibilidad.

### Arquitectura

La arquitectura del módulo de utilidades matemáticas está estructurada de la siguiente manera:

```
com.ultimatetoolbox.math/
├── core/                # Funciones matemáticas básicas
│   ├── MathUtils.java           # Utilidades matemáticas generales
│   ├── NumberUtils.java         # Utilidades para números
│   ├── Precision.java           # Control de precisión decimal
├── statistics/          # Funciones estadísticas
│   ├── StatUtils.java           # Utilidades estadísticas
│   ├── Descriptive.java         # Estadística descriptiva
│   ├── Regression.java          # Análisis de regresión
│   ├── Correlation.java         # Cálculo de correlaciones
├── conversion/          # Conversión de unidades
│   ├── UnitConverter.java       # Convertidor general
│   ├── LengthConverter.java     # Conversión de longitudes
│   ├── WeightConverter.java     # Conversión de pesos
│   ├── VolumeConverter.java     # Conversión de volúmenes
│   ├── TemperatureConverter.java # Conversión de temperaturas
│   ├── AreaConverter.java       # Conversión de áreas
│   ├── TimeConverter.java       # Conversión de tiempos
├── currency/            # Conversión de monedas
│   ├── CurrencyConverter.java   # Convertidor de monedas
│   ├── ExchangeRate.java        # Tasas de cambio
│   ├── CurrencyProvider.java    # Proveedor de datos de monedas
├── geometry/            # Cálculos geométricos
│   ├── GeometryUtils.java       # Utilidades geométricas
│   ├── Vector2D.java            # Vectores 2D
│   ├── Vector3D.java            # Vectores 3D
│   ├── Shapes.java              # Formas geométricas
├── finance/             # Cálculos financieros
│   ├── FinanceUtils.java        # Utilidades financieras
│   ├── Loan.java                # Cálculos de préstamos
│   ├── Investment.java          # Cálculos de inversiones
└── util/                # Utilidades generales
    ├── RandomUtils.java         # Generación de números aleatorios
    ├── InterpolationUtils.java  # Utilidades de interpolación
    ├── MatrixUtils.java         # Operaciones con matrices
```

### Características Principales (MVP)

#### 1. Operaciones Matemáticas Básicas

El sistema proporciona métodos para operaciones matemáticas comunes con mayor seguridad y control.

**Operaciones numéricas seguras:**
```java
// Suma segura (evita overflow)
long sum = MathUtils.safeAdd(Integer.MAX_VALUE, 10);

// Comparaciones con tolerancia para flotantes
boolean isEqual = MathUtils.equals(0.1 + 0.2, 0.3, 1e-10);

// Redondeo con control de precisión
double rounded = MathUtils.round(3.14159, 2); // 3.14
```

**Validación y transformación:**
```java
// Asegurar que un valor está en un rango
int bounded = MathUtils.clamp(value, 0, 100);

// Verificar si un número es primo
boolean isPrime = MathUtils.isPrime(17);

// Encontrar máximo y mínimo en arrays
double max = MathUtils.max(values);
double min = MathUtils.min(values);
```

#### 2. Estadística

El módulo incluye funciones estadísticas comunes para análisis de datos.

**Estadística descriptiva:**
```java
double[] data = {1.0, 2.0, 5.0, 7.0, 9.0};

// Medidas de tendencia central
double mean = StatUtils.mean(data);
double median = StatUtils.median(data);
double mode = StatUtils.mode(data);

// Medidas de dispersión
double variance = StatUtils.variance(data);
double stdDev = StatUtils.standardDeviation(data);
double range = StatUtils.range(data);

// Percentiles
double q1 = StatUtils.percentile(data, 25);
double q3 = StatUtils.percentile(data, 75);
```

**Regresión y correlación:**
```java
double[] x = {1, 2, 3, 4, 5};
double[] y = {2, 3, 5, 8, 11};

// Regresión lineal
RegressionResult result = Regression.linear(x, y);
double slope = result.getSlope();
double intercept = result.getIntercept();
double r2 = result.getRSquared();

// Predicción
double prediction = result.predict(6);

// Correlación
double correlation = Correlation.pearson(x, y);
```

**Resumen estadístico:**
```java
StatisticSummary summary = StatUtils.summary(data);
System.out.println("Media: " + summary.getMean());
System.out.println("Desviación estándar: " + summary.getStandardDeviation());
System.out.println("Mínimo: " + summary.getMin());
System.out.println("Máximo: " + summary.getMax());
```

#### 3. Conversión de Unidades

El sistema proporciona conversores para diferentes tipos de unidades físicas.

**Conversión de longitudes:**
```java
// Conversión simple
double meters = LengthConverter.convert(5, LengthUnit.FEET, LengthUnit.METERS);

// Fluent API
double cm = LengthConverter.from(3).inches().toCentimeters();

// Conversión con formato
String formatted = LengthConverter.from(1.5).kilometers()
    .toMiles().format("%.2f millas");
```

**Conversión de pesos:**
```java
double kg = WeightConverter.convert(150, WeightUnit.POUNDS, WeightUnit.KILOGRAMS);
double oz = WeightConverter.from(1).kilograms().toOunces();
```

**Conversión de temperaturas:**
```java
double fahrenheit = TemperatureConverter.celsiusToFahrenheit(20);
double kelvin = TemperatureConverter.celsiusToKelvin(20);
double celsius = TemperatureConverter.from(68).fahrenheit().toCelsius();
```

**Conversión de áreas:**
```java
double squareMeters = AreaConverter.convert(1, AreaUnit.ACRE, AreaUnit.SQUARE_METER);
double hectares = AreaConverter.from(10000).squareMeters().toHectares();
```

**Conversión de volúmenes:**
```java
double liters = VolumeConverter.convert(1, VolumeUnit.GALLON_US, VolumeUnit.LITER);
double ml = VolumeConverter.from(1).cups().toMilliliters();
```

**Sistema unificado de conversión:**
```java
// Conversión general entre unidades
double result = UnitConverter.convert(
    5.0,
    UnitCategory.LENGTH,
    "inch",
    "centimeter"
);
```

#### 4. Conversión de Monedas

El sistema proporciona herramientas para la conversión entre diferentes monedas.

**Conversión básica:**
```java
// Conversión con tasa fija
double euros = CurrencyConverter.convert(100, "USD", "EUR", 0.85);

// Con proveedor de tasas
CurrencyProvider provider = new FixedRateProvider();
double jpy = CurrencyConverter.withProvider(provider)
    .convert(100, "USD", "JPY");
```

**Tasas de cambio dinámicas:**
```java
// Configurar proveedor de tasas (podría usar API externa)
CurrencyProvider provider = new ApiExchangeRateProvider("api-key");
CurrencyConverter converter = CurrencyConverter.withProvider(provider);

// Convertir montos
double amount = converter.convert(100, "USD", "EUR");

// Formatear resultado
String formatted = converter.convertAndFormat(100, "USD", "EUR", 
    "%.2f EUR");
```

**Soporte para múltiples conversiones:**
```java
Map<String, Double> rates = CurrencyConverter.withProvider(provider)
    .convertToMultiple(1000, "USD", Arrays.asList("EUR", "GBP", "JPY"));
```

#### 5. Cálculos Geométricos

El módulo proporciona clases y métodos para cálculos geométricos comunes.

**Operaciones con vectores 2D:**
```java
// Crear vectores
Vector2D v1 = new Vector2D(3, 4);
Vector2D v2 = new Vector2D(1, 2);

// Operaciones vectoriales
Vector2D sum = v1.add(v2);
Vector2D diff = v1.subtract(v2);
Vector2D scaled = v1.multiply(2.0);
double magnitude = v1.magnitude();
double dot = v1.dot(v2);
```

**Formas geométricas:**
```java
// Cálculos con círculos
double area = Shapes.circleArea(5.0);
double circumference = Shapes.circleCircumference(5.0);

// Cálculos con rectángulos
double rectArea = Shapes.rectangleArea(4, 6);
double rectPerimeter = Shapes.rectanglePerimeter(4, 6);

// Cálculos con triángulos
double triangleArea = Shapes.triangleArea(3, 4, 5);
boolean isRightTriangle = Shapes.isRightTriangle(3, 4, 5, 1e-10);
```

**Distancias y ángulos:**
```java
// Distancia entre puntos
double distance = GeometryUtils.distance(x1, y1, x2, y2);

// Ángulo entre vectores
double angle = GeometryUtils.angleBetween(v1, v2);

// Conversión entre radianes y grados
double degrees = GeometryUtils.toDegrees(Math.PI / 4);
double radians = GeometryUtils.toRadians(45);
```

#### 6. Cálculos Financieros

El módulo incluye utilidades para cálculos financieros comunes.

**Préstamos e hipotecas:**
```java
// Calcular pago mensual de préstamo
double payment = FinanceUtils.calculateLoanPayment(
    200000,  // Principal
    0.04,    // Tasa de interés anual
    30       // Años
);

// Amortización de préstamo
List<LoanPayment> schedule = FinanceUtils.generateAmortizationSchedule(
    200000,  // Principal
    0.04,    // Tasa de interés anual
    30       // Años
);

// Calcular interés total
double totalInterest = FinanceUtils.calculateTotalInterest(
    200000,  // Principal
    0.04,    // Tasa de interés anual
    30       // Años
);
```

**Inversiones:**
```java
// Valor futuro de inversión única
double fv = FinanceUtils.calculateFutureValue(
    10000,   // Principal
    0.07,    // Tasa de interés anual
    10       // Años
);

// Valor futuro con aportaciones periódicas
double fvWithContributions = FinanceUtils.calculateFutureValueWithContributions(
    10000,   // Principal inicial
    500,     // Aportación mensual
    0.07,    // Tasa de interés anual
    10       // Años
);

// Calcular tasa interna de retorno (TIR)
double irr = FinanceUtils.calculateIRR(
    new double[] {-10000, 2000, 3000, 4000, 5000}
);
```

#### 7. Utilidades Aleatorias

El módulo proporciona generadores de números aleatorios con control mejorado.

```java
// Entero aleatorio en rango
int random = RandomUtils.nextInt(1, 100);

// Flotante aleatorio en rango
double randomDouble = RandomUtils.nextDouble(0.0, 1.0);

// Selección aleatoria de elementos
String selected = RandomUtils.randomElement(stringArray);

// Aleatorios con distribución específica
double gaussian = RandomUtils.nextGaussian(50.0, 10.0);
```

### Ejemplos de Uso Integrados

**Ejemplo 1: Análisis estadístico de datos financieros**
```java
// Datos de rendimiento
double[] returns = {0.05, 0.03, 0.08, -0.02, 0.06, 0.07};

// Análisis estadístico
double meanReturn = StatUtils.mean(returns);
double stdDev = StatUtils.standardDeviation(returns);
double sharpeRatio = meanReturn / stdDev;

// Proyección de inversión
double initialInvestment = 10000;
double projectedValue = FinanceUtils.calculateFutureValue(
    initialInvestment,
    meanReturn,
    10
);

// Conversión de moneda
double euroValue = CurrencyConverter.convert(
    projectedValue,
    "USD",
    "EUR",
    0.85
);

System.out.println("Rendimiento medio anual: " + 
    NumberUtils.format(meanReturn * 100, "%.2f%%"));
System.out.println("Volatilidad (desviación estándar): " + 
    NumberUtils.format(stdDev * 100, "%.2f%%"));
System.out.println("Ratio de Sharpe: " + 
    NumberUtils.format(sharpeRatio, "%.2f"));
System.out.println("Valor proyectado en 10 años: $" + 
    NumberUtils.format(projectedValue, "%.2f"));
System.out.println("Valor en euros: €" + 
    NumberUtils.format(euroValue, "%.2f"));
```

**Ejemplo 2: Cálculos de ingeniería**
```java
// Dimensiones en pulgadas
double lengthInches = 36;
double widthInches = 24;
double heightInches = 12;

// Conversión a centímetros
double lengthCm = LengthConverter.convert(
    lengthInches,
    LengthUnit.INCH,
    LengthUnit.CENTIMETER
);
double widthCm = LengthConverter.convert(
    widthInches, 
    LengthUnit.INCH,
    LengthUnit.CENTIMETER
);
double heightCm = LengthConverter.convert(
    heightInches,
    LengthUnit.INCH,
    LengthUnit.CENTIMETER
);

// Cálculo de volumen
double volumeCubicCm = lengthCm * widthCm * heightCm;
double volumeLiters = VolumeConverter.convert(
    volumeCubicCm,
    VolumeUnit.CUBIC_CENTIMETER,
    VolumeUnit.LITER
);

// Cálculo de peso si la densidad es 0.8 g/cm³
double weightGrams = volumeCubicCm * 0.8;
double weightKg = WeightConverter.convert(
    weightGrams,
    WeightUnit.GRAM,
    WeightUnit.KILOGRAM
);

System.out.println("Dimensiones: " + 
    lengthCm + " x " + widthCm + " x " + heightCm + " cm");
System.out.println("Volumen: " + 
    NumberUtils.format(volumeLiters, "%.2f") + " litros");
System.out.println("Peso estimado: " + 
    NumberUtils.format(weightKg, "%.2f") + " kg");
```

### Roadmap de Desarrollo Futuro

1. **MVP (3 meses):**
   - Implementación de operaciones matemáticas básicas
   - Funciones estadísticas comunes
   - Conversiones de unidades principales
   - Conversión de monedas con tasas fijas
   - Cálculos geométricos básicos
   - Cálculos financieros fundamentales

2. **Versiones Futuras:**
   - Más funciones estadísticas avanzadas
   - Integración con APIs para tasas de cambio de monedas en tiempo real
   - Cálculos financieros más complejos (opciones, derivados)
   - Implementación de álgebra lineal
   - Algoritmos numéricos avanzados
   - Visualización de datos matemáticos
   - Operaciones matemáticas paralelas para grandes conjuntos de datos

### Consideraciones Técnicas

- **Compatibilidad:** Java 8+
- **Precisión:** Atención especial a la precisión en cálculos con punto flotante
- **Rendimiento:** Optimizado para operaciones frecuentes
- **Thread-safety:** Seguro para uso en entornos multihilo
- **Internacionalización:** Soporte para diferentes formatos numéricos y de moneda
- **Extensibilidad:** Facilidad para añadir nuevas unidades y conversiones
