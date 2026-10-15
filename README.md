# E-Commerce Selenium Automation Framework

[![Selenium Automation Tests](https://github.com/RB-projects-15/ecommerce-selenium-automation-Copy/actions/workflows/maven-tests.yml/badge.svg)](https://github.com/RB-projects-15/ecommerce-selenium-automation-Copy/actions/workflows/maven-tests.yml)
![Java 21](https://img.shields.io/badge/Java-21-orange)
![Selenium 4.38](https://img.shields.io/badge/Selenium-4.38-green)
![TestNG 7.11](https://img.shields.io/badge/TestNG-7.11-red)

A UI test automation framework for the public demo shop [SauceDemo](https://www.saucedemo.com/), built with **Java 21, Selenium WebDriver, TestNG, Maven and the Page Object Model**. It covers login, product catalogue, cart, checkout and end-to-end purchase flows, runs in parallel on Chrome, Firefox or Edge, and produces an HTML report with failure screenshots. A GitHub Actions pipeline runs it headless on every push, pull request and nightly.

---

## Highlights

- **Page Object Model** with a shared `BasePage` (waits, safe click with JS fallback, typing helpers) and fluent navigation: each action returns the next page object (`loginAs(...)` returns `ProductsPage`, `checkout()` returns `CheckoutInformationPage`, and so on).
- **43 automated test executions** across 5 test classes: smoke, regression and negative groups, including data-driven tests.
- **Parallel execution**: one browser per test method through a `ThreadLocal` driver; tests are stateless so they are safe to run concurrently.
- **Cross-browser**: Chrome, Firefox and Edge, selectable from the command line.
- **Configuration without code changes**: `config.properties` plus `-D` overrides for browser, headless mode, base URL, timeouts and retry count.
- **Reporting**: ExtentReports HTML report with step logs, groups as categories, and a screenshot of every failure embedded in the report (it stays valid when downloaded from CI).
- **Flakiness control**: explicit waits only (no `Thread.sleep`), page objects that wait for their own page to load, and a configurable automatic retry of failed tests.
- **CI/CD**: GitHub Actions workflow with manual suite/browser selection, nightly schedule and report artifacts.

## Tech stack

| Area | Tool |
|---|---|
| Language | Java 21 |
| Browser automation | Selenium WebDriver 4.38 (Selenium Manager downloads the drivers) |
| Test framework | TestNG 7.11 |
| Build | Maven (Surefire 3.5.4) |
| Reporting | ExtentReports 5.1.2 + TestNG/Surefire reports |
| CI | GitHub Actions |

## Project structure

```
ecommerce-selenium-automation/
├── pom.xml
├── testng.xml                  # full regression suite (parallel)
├── testng-smoke.xml            # smoke suite (group "smoke")
├── .github/workflows/maven-tests.yml
└── src/test
    ├── java/com/qa/automation
    │   ├── base/               # BaseTest, TestGroups
    │   ├── config/             # ConfigReader (system property > config.properties)
    │   ├── driver/             # DriverManager (ThreadLocal, Chrome/Firefox/Edge)
    │   ├── pages/              # BasePage + one class per page
    │   ├── data/               # enums (User, Product, SortOption), constants, data providers
    │   ├── listeners/          # TestListener (report + screenshots), RetryAnalyzer/Transformer
    │   ├── utils/              # ExtentReportManager, ScreenshotUtils, PriceParser, XPaths
    │   └── tests/              # LoginTests, ProductsTests, CartTests, CheckoutTests, EndToEndPurchaseTests
    └── resources
        ├── config.properties
        └── META-INF/services/org.testng.ITestNGListener   # registers the listeners
```

## Test coverage

| Class | Scenarios |
|---|---|
| `LoginTests` (11) | valid login; 6 invalid-credential combinations (wrong password, unknown user, SQL-style input, empty fields) via data provider; locked-out user; direct access to `/inventory.html` without login; logout; password field masking |
| `ProductsTests` (13) | page layout; every product listed with the correct price; sorting by name and price in both directions (data-driven); details page for each of the 6 products (data-driven); back navigation |
| `CartTests` (9) | empty cart on a new session; add one product and verify name, price and quantity; add several products; remove from the cart page; remove from the Products page; continue shopping; cart persists while navigating; add from the details page; Reset App State |
| `CheckoutTests` (8) | form validation for first name, last name, postal code and all-empty (data-driven); cancel from the information page; overview lists the chosen items; item total, tax and grand total calculations; cancel from the overview |
| `EndToEndPurchaseTests` (2) | full purchase of several products with confirmation and empty cart afterwards; sort by price and buy the most expensive product |

Groups: `smoke` (6 tests, quick health check), `regression` (everything), `negative` (invalid input and access).

## Getting started

**Prerequisites:** JDK 21, Maven 3.9+, and Chrome, Firefox or Edge installed. No manual driver download is needed.

```bash
git clone https://github.com/RB-projects-15/ecommerce-selenium-automation-Copy.git
cd ecommerce-selenium-automation-Copy
mvn clean test
```

### Common commands

```bash
mvn clean test                                        # full regression, Chrome, visible browser
mvn clean test -DsuiteXmlFile=testng-smoke.xml        # smoke tests only
mvn clean test -Dheadless=true                        # headless (what CI uses)
mvn clean test -Dbrowser=firefox                      # firefox | edge | chrome
mvn clean test -DretryCount=0                         # disable retries while debugging
mvn clean test -Dtest=LoginTests                      # one class (overrides the suite file)
mvn clean test -Dtest=CartTests#verifyProductCanBeAddedToCart
```

### Configuration

Defaults live in `src/test/resources/config.properties`. Any key can be overridden with `-Dkey=value`.

| Key | Default | Description |
|---|---|---|
| `browser` | `chrome` | `chrome`, `firefox` or `edge` |
| `headless` | `false` | run without a visible browser window |
| `baseUrl` | `https://www.saucedemo.com/` | application under test |
| `explicitWait` | `10` | explicit wait timeout in seconds |
| `retryCount` | `1` | re-runs of a failed test before it is reported as failed |

Parallelism is set in the suite files (`parallel="methods" thread-count="3"`).

## Reports

After a run:

| Output | Location |
|---|---|
| Extent HTML report (open in a browser) | `target/extent-report/ExtentReport.html` |
| Failure screenshots (PNG) | `target/screenshots/` |
| TestNG / Surefire reports | `target/surefire-reports/` |

In CI the same three folders are uploaded as the `selenium-test-reports` artifact (kept for 14 days).

## CI/CD

`.github/workflows/maven-tests.yml` runs on every push and pull request to `main`, nightly on weekdays, and on demand (**Actions → Run workflow**, where you can pick the suite and browser). The job uses Java 21, Maven caching, headless mode, a 20-minute timeout and read-only repository permissions.

## Design decisions

- **Everything is under `src/test`.** This is a test-only project, so all dependencies are `test` scoped and nothing test-related ships in `src/main`.
- **Pages wait for themselves.** Each page object constructor waits for its URL and a key element. A broken navigation fails at the step that caused it with a clear timeout, not several steps later.
- **No state in test classes.** The WebDriver is fetched through `DriverManager` per thread, never stored in a field, which makes `parallel="methods"` safe.
- **Resilient clicks.** `BasePage.click` waits for the element to be clickable and falls back to a JavaScript click if the element is covered or re-rendered. `addToCart` also waits for the button to change to "Remove", so the next step never races the UI.
- **Password manager popups are disabled** in Chrome/Edge (`DriverManager`), because the breach warning for SauceDemo's public password can swallow clicks.
- **Typed test data.** Products, users and sort options are enums, so expected names and prices live in one place instead of being repeated in assertions.
- **One reporting path.** `TestListener` is the single place that builds the report and takes screenshots. It is registered via `META-INF/services`, so it also works when a single test is run from the IDE.

## Adding a new test

1. Add or reuse a page object in `pages/` (locators as `private static final By`, actions returning the next page).
2. Create a class in `tests/` that extends `BaseTest`; put no instance fields in it.
3. Annotate tests with `@Test(groups = {...}, description = "...")` using the constants in `TestGroups`.
4. Put reusable inputs in `data/` (enum or `@DataProvider`).

## Notes

- The target site is a public demo application; the credentials in `TestData` are its published demo values, not secrets.
- If SauceDemo changes its markup, only the locators in `pages/` need updating.
