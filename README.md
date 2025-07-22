# OMS Migration Test Automation

This project contains automated tests for the OMS (Order Management System) migration process, specifically focusing on catalog management and city management functionalities.

## Project Structure
```
src/
├── main/
│   └── java/org/example/
│       └── utils/           # Utility classes and helpers
└── test/
    └── java/org/example/
        ├── Feature_Test/    # Test classes
        └── pages/           # Page Object Model classes
```

## Features
- Automated testing of OMS migration scenarios
- Page Object Model (POM) design pattern
- Extent Reports integration for test reporting
- Data-driven testing support

## Prerequisites
- Java JDK 11 or higher
- Maven
- Chrome/Firefox browser
- WebDriver

## Setup
1. Clone the repository
2. Install dependencies:
   ```
   mvn clean install
   ```
3. Configure test data in `src/test/resources/testdata/`

## Running Tests
To run all tests:
```
mvn test
```

To run a specific test class:
```
mvn test -Dtest=OMS_MIGRATION
```

## Test Reports
Test reports are generated in the `test-output` directory after test execution.

## Configuration
- Browser settings can be configured in `src/test/resources/config.properties`
- Test data is managed in `src/test/resources/testdata/`

## Best Practices
- Follow Page Object Model (POM) design pattern
- Use explicit waits for better test stability
- Keep test methods independent and atomic
- Use descriptive test method names
- Add proper assertions for test validations

## Troubleshooting
- Ensure WebDriver is compatible with the browser version
- Check console logs for any initialization errors
- Verify test data is correctly formatted

## Contributing
Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
