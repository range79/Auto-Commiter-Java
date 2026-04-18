Auto-Commiter-Java is a high-performance bot designed to automate and randomize your Git activity. It maintains an active contribution history by automatically generating and pushing random commits to your GitHub repositories.

## How it Works

The application follows a structured automation workflow:
1. **Initialization**: Validates your GitHub credentials (SSH) and repository URL.
2. **Local Sync**: If the repository is not present locally, it performs a clean clone.
3. **Random Commit Generation**: Automatically generates random changes and creates unique commits to simulate real activity.
4. **Push to Remote**: Securely pushes these random commits back to your GitHub repository using SSH.

## Configuration (Environment Variables)

The application requires the following environment variables to be set:

| Variable | Description |
|----------|-------------|
| `GITHUB_REPO_URL` | The SSH URL of your repository (e.g., `git@github.com:user/repo.git`). |
| `GITHUB_SSH_KEY_PATH` | The local filesystem path to your private SSH key. |

## Fast CI/CD Setup

To run this automation as a scheduled task in GitHub Actions, it is recommended to commit the pre-built native binary directly to your repository for maximum speed.

1. **Build and Commit the Binary**:
   ```bash
   ./gradlew nativeCompile
   cp build/native/nativeCompile/auto-commiter .
   git add auto-commiter && git commit -m "Add automation binary" && git push
   ```

2. **Add Workflow File** (`.github/workflows/auto.yml`):
   ```yaml
   name: Auto Commit
   on:
     schedule: [{cron: '0 * * * *'}] # Runs every hour
     workflow_dispatch:
   jobs:
     run:
       runs-on: ubuntu-latest
       steps:
         - uses: actions/checkout@v4
         - name: Run Automation
           run: |
             echo "${{ secrets.SSH_KEY }}" > id_rsa && chmod 600 id_rsa
             chmod +x auto-commiter
             ./auto-commiter
           env:
             GITHUB_REPO_URL: ${{ secrets.REPO_URL }}
             GITHUB_SSH_KEY_PATH: "./id_rsa"
   ```

3. **Required Secrets**:
   - `SSH_KEY`: Your private SSH key content.
   - `REPO_URL`: Your repository SSH URL.
