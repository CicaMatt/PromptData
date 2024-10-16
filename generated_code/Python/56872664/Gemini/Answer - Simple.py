import pandas as pd
from sklearn.model_selection import train_test_split

# Assuming your data is in a DataFrame called `df`

# 1. Group by Subject
grouped_data = df.groupby('subject_id')

# 2. Prepare for Stratification 
# Create a new DataFrame where each row represents a subject and their dominant category
subject_category_df = pd.DataFrame({
    'subject_id': grouped_data.groups.keys(),
    'dominant_category': grouped_data['category'].agg(lambda x: x.value_counts().index[0]) 
})

# 3. Stratified Split of Subjects
train_subjects, temp_subjects = train_test_split(
    subject_category_df['subject_id'], 
    test_size=0.4,  #  Remaining 40% for validation and test
    stratify=subject_category_df['dominant_category']
)

val_subjects, test_subjects = train_test_split(
    temp_subjects, 
    test_size=0.5, 
    stratify=subject_category_df[subject_category_df['subject_id'].isin(temp_subjects)]['dominant_category']
)

# 4. Create Final Datasets
train_data = df[df['subject_id'].isin(train_subjects)]
val_data = df[df['subject_id'].isin(val_subjects)]
test_data = df[df['subject_id'].isin(test_subjects)]

# Optional: Shuffle within each set if needed
# train_data = train_data.sample(frac=1) 
# ...