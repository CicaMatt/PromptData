import pandas as pd
from sklearn.model_selection import train_test_split
from collections import Counter

# Sample data
data = pd.DataFrame({
    'note_id': [1, 2, 3, 4, 5, 6],
    'subject_id': [1, 1, 1, 2, 2, 3],
    'category': ['ECG', 'Discharge', 'Nursing', 'Nursing', 'Nursing', 'ECG'],
    'note': ['blah ...']*6
})

# Step 1: Group by subject_id and find the most common category per subject
subject_grouped = data.groupby('subject_id').agg({
    'category': lambda x: Counter(x).most_common(1)[0][0],  # Most common category for the subject
    'note_id': 'count'  # Number of notes per subject
}).reset_index()

# Step 2: Stratify by the most common category
X = subject_grouped['subject_id']
y = subject_grouped['category']  # Use the predominant category per subject for stratification

# Step 3: Split into train, validation, and test sets using stratified splitting
# First, split into train and temp (validation + test)
train_subjects, temp_subjects = train_test_split(X, test_size=0.4, stratify=y, random_state=42)

# Now split temp into validation and test
temp_subject_grouped = subject_grouped[subject_grouped['subject_id'].isin(temp_subjects)]
y_temp = temp_subject_grouped['category']
val_subjects, test_subjects = train_test_split(temp_subjects, test_size=0.5, stratify=y_temp, random_state=42)

# Step 4: Assign rows based on the subject splits
train_data = data[data['subject_id'].isin(train_subjects)]
val_data = data[data['subject_id'].isin(val_subjects)]
test_data = data[data['subject_id'].isin(test_subjects)]

# Check results
print("Training set subjects:", train_subjects)
print("Validation set subjects:", val_subjects)
print("Test set subjects:", test_subjects)

# Optional: check the proportions of categories in each set to verify stratification
print("\nCategory proportions in training set:\n", train_data['category'].value_counts(normalize=True))
print("\nCategory proportions in validation set:\n", val_data['category'].value_counts(normalize=True))
print("\nCategory proportions in test set:\n", test_data['category'].value_counts(normalize=True))
