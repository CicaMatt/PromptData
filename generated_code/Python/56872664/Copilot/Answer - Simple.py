import pandas as pd
from sklearn.model_selection import StratifiedShuffleSplit

# Sample data
data = {
    'note_id': [1, 2, 3, 4, 5, 6],
    'subject_id': [1, 1, 1, 2, 2, 3],
    'category': ['ECG', 'Discharge', 'Nursing', 'Nursing', 'Nursing', 'ECG'],
    'note': ['blah ...', 'blah ...', 'blah ...', 'blah ...', 'blah ...', 'blah ...']
}
df = pd.DataFrame(data)

# Step 1: Group by subject_id
subject_groups = df.groupby('subject_id').first().reset_index()

# Step 2: Create a new DataFrame for stratified split
subject_groups = subject_groups[['subject_id', 'category']]

# Step 3: Perform stratified split
split = StratifiedShuffleSplit(n_splits=1, test_size=0.4, random_state=42)
for train_index, temp_index in split.split(subject_groups, subject_groups['category']):
    train_subjects = subject_groups.loc[train_index]
    temp_subjects = subject_groups.loc[temp_index]

# Further split temp_subjects into validation and test sets
split = StratifiedShuffleSplit(n_splits=1, test_size=0.5, random_state=42)
for val_index, test_index in split.split(temp_subjects, temp_subjects['category']):
    val_subjects = temp_subjects.loc[val_index]
    test_subjects = temp_subjects.loc[test_index]

# Step 4: Map the split back to the original DataFrame
train_set = df[df['subject_id'].isin(train_subjects['subject_id'])]
val_set = df[df['subject_id'].isin(val_subjects['subject_id'])]
test_set = df[df['subject_id'].isin(test_subjects['subject_id'])]

# Display the results
print("Training Set:\n", train_set)
print("Validation Set:\n", val_set)
print("Test Set:\n", test_set)
