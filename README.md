# Auto-Commiter-Java

Auto-Commiter-Java is a high-performance bot designed to automate and randomize your Git activity. It maintains an active contribution history by automatically generating and pushing random commits to your GitHub repositories.

## How it Works

The application follows a structured automation workflow:
1. **Initialization**: Validates your GitHub credentials (SSH) and repository URL.
2. **Local Sync**: If the repository is not present locally, it performs a clean clone.
3. **Random Commit Generation**: Automatically generates random changes (appending to `.automation_log`) and creates unique commits to simulate real activity.
4. **Push to Remote**: Securely pushes these random commits back to your GitHub repository using SSH.

## Configuration (Environment Variables)

The application requires the following environment variables to be set:

| Variable | Description |
|----------|-------------|
| `REPO_URL` | The SSH URL of your repository (e.g., `git@github.com:user/repo.git`). |
| `SSH_KEY_PATH` | The local filesystem path to your private SSH key. |

## Quick Start with GitHub Actions

To run this bot automatically on an hourly schedule using GitHub Actions:

1. **Prepare the Binary**:
   Compile the project locally and copy the resulting binary to the root directory:
   ```bash
   ./gradlew nativeCompile
   cp build/native/nativeCompile/auto-commiter .
   ```

2. **Add Binary to Repository**:
   Push the binary to your repository:
   ```bash
   git add auto-commiter
   git commit -m "Add auto-commiter binary"
   git push
   ```

3. **Define SSH Key Secret**:
   In your GitHub Repository Settings (**Settings > Secrets and variables > Actions**), create a secret named `SSH_KEY` and paste your private SSH key content there.

4. **Add Workflow Example**:
   Create a file at `.github/workflows/auto.yml` and paste the following configuration:

```yaml
on:
  schedule:
    - cron: '0 * * * *'
  workflow_dispatch:

jobs:
  run:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - name: Setup SSH
        run: |
          mkdir -p ~/.ssh
          echo "${{ secrets.SSH_KEY }}" > ~/.ssh/id_rsa
          chmod 600 ~/.ssh/id_rsa
          ssh-keyscan github.com >> ~/.ssh/known_hosts

      - name: Run Automation
        run: |
          # Switch remote from HTTPS to SSH so the bot can use the SSH Key
          git remote set-url origin git@github.com:${{ github.repository }}.git
          
          chmod +x auto-commiter
          ./auto-commiter
        env:
          REPO_URL: git@github.com:${{ github.repository }}.git
          SSH_KEY_PATH: ~/.ssh/id_rsa
```
