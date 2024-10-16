import pandas as pd
from sklearn.model_selection import StratifiedShuffleSplit

def split_data_with_subject_integrity(data, category_column, subject_column, train_ratio=0.6, val_ratio=0.2, test_ratio=0.2):
    """
    Splits a dataset into training, validation, and test sets, ensuring:
    - Stratification based on a category column.
    - All observations from a given subject are in the same split.
    - Shuffling of subjects within each split.

    Args:
        data: The DataFrame containing the data.
        category_column: The column name for stratification.
        subject_column: The column name identifying subjects.
        train_ratio: Proportion of data for training.
        val_ratio: Proportion of data for validation.
        test_ratio: Proportion of data for testing.

    Returns:
        train_data, val_data, test_data: The split DataFrames.
    """

    # Create a DataFrame of unique subjects and their corresponding categories
    unique_subjects = data[[subject_column, category_column]].drop_duplicates()

    # Stratify the subjects based on their categories
    splitter = StratifiedShuffleSplit(n_splits=1, test_size=1 - train_ratio, random_state=42)
    for train_index, temp_index in splitter.split(unique_subjects, unique_subjects[category_column]):
        train_subjects = unique_subjects.iloc[train_index][subject_column]
        temp_subjects = unique_subjects.iloc[temp_index][subject_column]

    # Further split the temp_subjects into validation and test sets
    splitter = StratifiedShuffleSplit(n_splits=1, test_size=test_ratio / (val_ratio + test_ratio), random_state=42)
    for val_index, test_index in splitter.split(temp_subjects, temp_subjects.map(unique_subjects.set_index(subject_column)[category_column])):
        val_subjects = temp_subjects.iloc[val_index]
        test_subjects = temp_subjects.iloc[test_index]

    # Create the final splits by filtering the original data based on the subject splits
    train_data = data[data[subject_column].isin(train_subjects)]
    val_data = data[data[subject_column].isin(val_subjects)]
    test_data = data[data[subject_column].isin(test_subjects)]

    return train_data, val_data, test_data

# Example usage
# Assuming your data is in a DataFrame called 'df'
train_data, val_data, test_data = split_data_with_subject_integrity(df, 'category', 'subject_id')