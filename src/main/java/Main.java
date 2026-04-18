import config.RepositoryPropertiesFactory;
import service.GitAutomationService;

void main() {
    RepositoryPropertiesFactory properties = new RepositoryPropertiesFactory();
    GitAutomationService service = new GitAutomationService(properties.readRepositoryProperties());
    service.run();
}