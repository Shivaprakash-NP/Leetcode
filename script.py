import os
import requests
import json
import time
import subprocess
from datetime import datetime

# ==== CONFIGURATION ====

# Try to load from environment, else fallback to hardcoded session token
LEETCODE_SESSION = os.getenv("LEETCODE_SESSION") or "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfYXV0aF91c2VyX2lkIjoiMTQ3MDI0NDQiLCJfYXV0aF91c2VyX2JhY2tlbmQiOiJhbGxhdXRoLmFjY291bnQuYXV0aF9iYWNrZW5kcy5BdXRoZW50aWNhdGlvbkJhY2tlbmQiLCJfYXV0aF91c2VyX2hhc2giOiJiMjVkMTYxYmYyYTg3NTkzZDE1YzYyODIzZDFhMWJiMTc5ZGRkMmJmZTdiYjUyMmJhNDcyNDAxNTIwZGRiNmM4Iiwic2Vzc2lvbl91dWlkIjoiZWIwNzNiODciLCJpZCI6MTQ3MDI0NDQsImVtYWlsIjoic2hpdmFwcmFrYXNobnAxQGdtYWlsLmNvbSIsInVzZXJuYW1lIjoic2hpdmFfX19ucCIsInVzZXJfc2x1ZyI6InNoaXZhX19fbnAiLCJhdmF0YXIiOiJodHRwczovL2Fzc2V0cy5sZWV0Y29kZS5jb20vdXNlcnMvc2hpdmFfX19ucC9hdmF0YXJfMTczNjE4MDc0OS5wbmciLCJyZWZyZXNoZWRfYXQiOjE3NTA2NjE3NzksImlwIjoiMjQwMTo0OTAwOjdiOWY6NzZjNjphMjQwOjdkYWI6NjEyODoyZDQ4IiwiaWRlbnRpdHkiOiIwNmI0YTdlNjI3NGMxNjcxMGExZjZhYzdhZTA5ZWZmOSIsImRldmljZV93aXRoX2lwIjpbIjFlZTM2ZDljODI2ZWI3YTc0NmQ1YWFjMTkyODBjMzE1IiwiMjQwMTo0OTAwOjdiOWY6NzZjNjphMjQwOjdkYWI6NjEyODoyZDQ4Il0sIl9zZXNzaW9uX2V4cGlyeSI6MTIwOTYwMH0.D4jQ3GBqWqspCu1sFHUbAvcSUPKRt_OHOV6YVlcYktg"

HEADERS = {
    "Content-Type": "application/json",
    "Referer": "https://leetcode.com",
    "Origin": "https://leetcode.com",
    "User-Agent": "Mozilla/5.0",
    "Cookie": f"LEETCODE_SESSION={LEETCODE_SESSION}"
}

# Map difficulty to folders
DIFFICULTY_FOLDERS = {
    "EASY": "Easy",
    "MEDIUM": "Medium",
    "HARD": "Hard"
}

# Your repo path
REPO_DIR = os.path.expanduser("~/LeetCodeSolutions")

# Create folders if they don't exist
for folder in DIFFICULTY_FOLDERS.values():
    os.makedirs(os.path.join(REPO_DIR, folder), exist_ok=True)

# ==== FUNCTIONS ====

def get_accepted_submissions():
    query = {
        "operationName": "submissionList",
        "variables": {
            "offset": 0,
            "limit": 50,
            "questionSlug": ""
        },
        "query": """
        query submissionList($offset: Int!, $limit: Int!, $questionSlug: String!) {
            submissionList(offset: $offset, limit: $limit, questionSlug: $questionSlug) {
                submissions {
                    id
                    title
                    titleSlug
                    statusDisplay
                    lang
                }
            }
        }
        """
    }
    res = requests.post("https://leetcode.com/graphql", headers=HEADERS, json=query)
    data = res.json()
    return [s for s in data.get("data", {}).get("submissionList", {}).get("submissions", []) 
            if s["statusDisplay"] == "Accepted" and s["lang"].lower() == "java"]

def get_submission_code(submission_id):
    query = '''
    query submissionDetails($id: Int!) {
      submissionDetails(submissionId: $id) {
        id
        code
        runtime
        memory
        timestamp
        lang {
          name
        }
        question {
          titleSlug
          title
          difficulty
        }
      }
    }
    '''
    variables = {'id': int(submission_id)}  # Cast to int!
    res = requests.post(
        'https://leetcode.com/graphql/',
        json={'query': query, 'variables': variables},
        headers=HEADERS
    )

    try:
        json_data = res.json()
        if "data" in json_data and json_data["data"]["submissionDetails"]:
            return json_data["data"]["submissionDetails"]
        else:
            print(f"[!] Failed to fetch details for submission {submission_id}")
            print(f"Status: {res.status_code}, Response: {json_data}")
            return None
    except Exception as e:
        print(f"[!] Exception for submission {submission_id}: {e}")
        return None
        
def save_solution(code, question):
    difficulty_folder = DIFFICULTY_FOLDERS[question["difficulty"].upper()]
    filename = f"{question['titleSlug'].lower().replace('-', '_')}.java"
    filepath = os.path.join(REPO_DIR, difficulty_folder, filename)

    # Avoid overwriting existing solutions
    if os.path.exists(filepath):
        return None

    with open(filepath, "w") as f:
        f.write(code)

    return filepath

def git_commit_push(file_path, title):
    rel_path = os.path.relpath(file_path, REPO_DIR)
    subprocess.run(["git", "-C", REPO_DIR, "add", rel_path])
    subprocess.run(["git", "-C", REPO_DIR, "commit", "-m", f"Add solution for {title}"])
    subprocess.run(["git", "-C", REPO_DIR, "push", "--set-upstream", "origin", "master"])

def main():
    print(f"Checking at {datetime.now()}...")
    submissions = get_accepted_submissions()
    for sub in submissions:
        detail = get_submission_code(sub["id"])
        if not detail:
            continue
        file_path = save_solution(detail["code"], detail["question"])
        if file_path:
            git_commit_push(file_path, detail["question"]["title"])
            print(f"[+] Synced: {detail['question']['title']}")

# ==== ENTRY POINT ====
if __name__ == "__main__":
    main()

