import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IAutoModeration, defaultValue } from 'app/shared/model/bot/auto-moderation.model';

export const ACTION_TYPES = {
  FETCH_AUTOMODERATION_LIST: 'autoModeration/FETCH_AUTOMODERATION_LIST',
  FETCH_AUTOMODERATION: 'autoModeration/FETCH_AUTOMODERATION',
  CREATE_AUTOMODERATION: 'autoModeration/CREATE_AUTOMODERATION',
  UPDATE_AUTOMODERATION: 'autoModeration/UPDATE_AUTOMODERATION',
  DELETE_AUTOMODERATION: 'autoModeration/DELETE_AUTOMODERATION',
  RESET: 'autoModeration/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAutoModeration>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AutoModerationState = Readonly<typeof initialState>;

// Reducer

export default (state: AutoModerationState = initialState, action): AutoModerationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODERATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODERATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AUTOMODERATION):
    case REQUEST(ACTION_TYPES.UPDATE_AUTOMODERATION):
    case REQUEST(ACTION_TYPES.DELETE_AUTOMODERATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODERATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODERATION):
    case FAILURE(ACTION_TYPES.CREATE_AUTOMODERATION):
    case FAILURE(ACTION_TYPES.UPDATE_AUTOMODERATION):
    case FAILURE(ACTION_TYPES.DELETE_AUTOMODERATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODERATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODERATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AUTOMODERATION):
    case SUCCESS(ACTION_TYPES.UPDATE_AUTOMODERATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AUTOMODERATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/auto-moderations';

// Actions

export const getEntities: ICrudGetAllAction<IAutoModeration> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_AUTOMODERATION_LIST,
  payload: axios.get<IAutoModeration>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAutoModeration> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AUTOMODERATION,
    payload: axios.get<IAutoModeration>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAutoModeration> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AUTOMODERATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAutoModeration> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AUTOMODERATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAutoModeration> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AUTOMODERATION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
